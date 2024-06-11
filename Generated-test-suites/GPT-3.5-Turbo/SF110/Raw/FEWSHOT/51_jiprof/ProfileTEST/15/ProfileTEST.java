
package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ProfileTest {

    @Before
    public void setUp() {
        Profile.initProfiler();
    }

    @Test
    public void testClear() {
        Profile.clear();
        
        assertNull(Profile._threadDictionary);
        assertNull(Profile._frameList);
        assertNull(Profile._threadActiveFrame);
        assertNull(Profile._methodDictionary);
        assertNull(Profile._lock);
        assertNull(Profile._allocList);
        assertNull(Profile._controller);
        assertNull(Profile._controllerThread);
    }
    
    @Test
    public void testStartAndStop() {
        Profile.start();
        assertEquals(true, Profile._controller._isRunning);
        
        Profile.stop();
        assertEquals(false, Profile._controller._isRunning);
    }
    
    @Test
    public void testSetFileName() {
        Profile.setFileName("testFileName");
        assertEquals("testFileName", Profile._controller._fileName);
    }
    
    @Test
    public void testShutdown() {
        Profile.shutdown();
        
        assertEquals(false, Profile._controller._profile);
        assertNull(Profile._threadDictionary.getMostRecentFrame(1L));
        assertNull(Profile._threadDictionary.getMostRecentFrame(2L));
        assertNull(Profile.frameList());
    }
    
    @Test
    public void testThreads() {
        Iterable<Long> threads = Profile.threads();
        
        // Add assertions here
    }
    
    @Test
    public void testInteractions() {
        Iterable<Frame> interactions = Profile.interactions(1L);
        
        // Add assertions here
    }
    
    @Test
    public void testFrameList() {
        Iterable<Frame> frameList = Profile.frameList();
        
        // Add assertions here
    }
    
    @Test
    public void testAllocations() {
        Iterable<ClassAllocation> allocations = Profile.allocations();
        
        // Add assertions here
    }
    
    @Test
    public void testGetThreadTotalTime() {
        long totalTime = Profile.getThreadTotalTime(1L);
        
        // Add assertions here
    }
    
    @Test
    public void testSortFrameList() {
        Comparator<Frame> comp = new Comparator<Frame>() {
            public int compare(Frame f1, Frame f2) {
                // Add logic here
                return 0;
            }
        };
        
        Profile.sortFrameList(comp);
        
        // Add assertions here
    }
    
    @Test
    public void testStartMethod() {
        Profile.start("className", "methodName");
        
        // Add assertions here
    }
    
    @Test
    public void testEndMethod() {
        Profile.end("className", "methodName");
        
        // Add assertions here
    }
    
    @Test
    public void testBeginWait() {
        Profile.beginWait("className", "methodName");
        
        // Add assertions here
    }
    
    @Test
    public void testEndWait() {
        Profile.endWait("className", "methodName");
        
        // Add assertions here
    }
    
    @Test
    public void testUnwind() {
        Profile.unwind("className", "methodName", "exception");
        
        // Add assertions here
    }
    
    @Test
    public void testAlloc() {
        Profile.alloc("className");
        
        // Add assertions here
    }
    
    @Test
    public void testRun() throws IOException {
        Profile._threadDictionary = new ThreadDictionary();
        Profile._frameList = new ArrayList<Frame>();
        Profile._controller = new Controller();
        Profile._controllerThread = new Thread(new Profile());
        
        Profile.run();
        
        // Add assertions here
    }

}
