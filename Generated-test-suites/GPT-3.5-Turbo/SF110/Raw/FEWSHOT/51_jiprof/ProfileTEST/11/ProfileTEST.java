
package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;


public class ProfileTest {
    
    @Before
    public void setup() {
        Profile.init();
    }

    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        
        // Verify that the controller start method is called
        verify(Profile._controller, times(1)).start();
        
        // Verify that the controller file name is set
        verify(Profile._controller, times(1)).setFileName(anyString());
    }

    @Test
    public void testInit() {
        Profile.init();
        
        // Verify that all static fields are re-initialized
        assertNull(Profile._threadActiveFrame);
        assertNull(Profile._threadDictionary);
        assertNull(Profile._methodDictionary);
        assertNull(Profile._frameList);
        assertNull(Profile._allocList);
    }

    @Test
    public void testClear() {
        Profile.init();
        Profile.clear();
        
        // Verify that all static fields are re-initialized
        assertNull(Profile._threadActiveFrame);
        assertNull(Profile._threadDictionary);
        assertNull(Profile._methodDictionary);
        assertNull(Profile._frameList);
        assertNull(Profile._allocList);
    }

    @Test
    public void testStart() {
        Profile.start();
        
        // Verify that the controller start method is called
        verify(Profile._controller, times(1)).start();
    }

    @Test
    public void testStop() {
        Profile.stop();
        
        // Verify that the controller stop method is called
        verify(Profile._controller, times(1)).stop();
    }

    @Test
    public void testSetFileName() {
        Profile.setFileName("testfile.txt");
        
        // Verify that the controller file name is set
        verify(Profile._controller, times(1)).setFileName("testfile.txt");
    }

    @Test
    public void testShutdown() {
        Profile.shutdown();
        
        // Verify that the controller profile flag is set to false
        assertEquals(false, Profile._profile);
        
        // Verify that the close method is called for each frame
        for (Frame frame : Profile.frameList()) {
            verify(frame, times(1)).close();
        }
        
        // Verify that the computeNetTime method is called for each frame
        for (Frame frame : Profile.frameList()) {
            verify(frame, times(1)).computeNetTime();
        }
    }

    @Test
    public void testThreads() {
        Iterable<Long> threads = Profile.threads();
        
        // Verify that the threads method returns the thread dictionary's threads
        assertEquals(Profile._threadDictionary.threads(), threads);
    }

    @Test
    public void testInteractions() {
        Iterable<Frame> interactions = Profile.interactions(Thread.currentThread().getId());
        
        // Verify that the interactions method returns the thread dictionary's interactions for the given thread id
        assertEquals(Profile._threadDictionary.interactions(Thread.currentThread().getId()), interactions);
    }

    @Test
    public void testFrameList() {
        Iterable<Frame> frameList = Profile.frameList();
        
        // Verify that the frameList method returns the frame list
        assertEquals(Profile._frameList, frameList);
    }

    @Test
    public void testAllocations() {
        Iterable<ClassAllocation> allocations = Profile.allocations();
        
        // Verify that the allocations method returns the allocation list values
        assertEquals(Profile._allocList.values(), allocations);
    }

    @Test
    public void testGetThreadTotalTime() {
        long totalTime = Profile.getThreadTotalTime(Thread.currentThread().getId());
        
        // Verify that the getThreadTotalTime method returns the thread dictionary's total time for the given thread id
        assertEquals(Profile._threadDictionary.getThreadTotalTime(Thread.currentThread().getId()), totalTime);
    }

    @Test
    public void testSortFrameList() {
        Profile.sortFrameList(null);
        
        // Verify that the frame list is sorted using the given comparator
        verify(Collections, times(1)).sort(Profile._frameList, null);
    }

    @Test
    public void testStart() {
        Profile.start("ClassName", "MethodName");
        
        // Verify that the method dictionary is updated with the new method if it doesn't exist
        verify(Profile._methodDictionary, times(1)).put(anyString(), any(Method.class));
        
        // Verify that a new frame is added to the frame list and thread dictionary
        verify(Profile._frameList, times(1)).add(any(Frame.class));
        verify(Profile._threadDictionary, times(1)).add(anyLong(), any(Frame.class));
        
        // Verify that the thread active frame is updated with the new frame
        verify(Profile._threadActiveFrame, times(1)).put(anyLong(), any(Frame.class));
    }

    @Test
    public void testEnd() {
        Profile.end("ClassName", "MethodName");
        
        // Verify that the thread active frame is updated with the parent frame
        verify(Profile._threadActiveFrame, times(1)).put(anyLong(), any(Frame.class));
        
        // Verify that the end time is set for the target frame
        verify(any(Frame.class), times(1)).setEndTime(anyLong());
    }

    @Test
    public void testBeginWait() {
        Profile.beginWait("ClassName", "MethodName");
        
        // Verify that the begin wait time is set for the target frame
        verify(any(Frame.class), times(1)).beginWait(anyLong());
    }

    @Test
    public void testEndWait() {
        Profile.endWait("ClassName", "MethodName");
        
        // Verify that the end wait time is set for the target frame
        verify(any(Frame.class), times(1)).endWait(anyLong());
    }

    @Test
    public void testUnwind() {
        Profile.unwind("ClassName", "MethodName", "Exception");
        
        // Verify that the thread active frame is updated with the target frame
        verify(Profile._threadActiveFrame, times(1)).put(anyLong(), any(Frame.class));
    }

    @Test
    public void testAlloc() {
        Profile.alloc("ClassName");
        
        // Verify that the allocation count is incremented for the class allocation
        verify(any(ClassAllocation.class), times(1)).incAllocCount();
    }

    @Test
    public void testRun() {
        Profile.run();
        
        // Verify that the shutdown method is called
        verify(Profile, times(1)).shutdown();
        
        // Verify that the dump method is called
        verify(ProfileDump, times(1)).dump();
        
        // Verify that the close method is called for the controller
        verify(Profile._controller, times(1)).close();
        
        // Verify that the controller thread is interrupted
        verify(Profile._controllerThread, times(1)).interrupt();
    }

}
