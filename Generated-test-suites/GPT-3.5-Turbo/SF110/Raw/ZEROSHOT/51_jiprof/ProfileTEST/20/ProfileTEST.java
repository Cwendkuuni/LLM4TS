package com.mentorgen.tools.profile.runtime;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class ProfileTest {
    
    @Before
    public void setUp() {
        Profile.init();
    }
    
    @Test
    public void testInit() {
        Profile.initProfiler();
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }
    
    @Test
    public void testClear() {
        Profile.clear();
        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }
    
    @Test
    public void testStart() {
        Profile.start();
        assertNotNull(Profile._controller);
        assertTrue(Profile._controller.isRunning());
    }
    
    @Test
    public void testStop() {
        Profile.stop();
        assertNotNull(Profile._controller);
        assertFalse(Profile._controller.isRunning());
    }
    
    @Test
    public void testSetFileName() {
        String fileName = "test.txt";
        Profile.setFileName(fileName);
        assertEquals(fileName, Profile._controller.getFileName());
    }
    
    @Test
    public void testShutdown() {
        Profile.shutdown();
        assertFalse(Controller._profile);
        for (Long threadId : Profile._threadDictionary.threads()) {
            Frame f = Profile._threadDictionary.getMostRecentFrame(threadId);
            assertTrue(f.isClosed());
            f.computeNetTime();
        }
        for (Frame frame : Profile.frameList()) {
            frame.computeNetTime();
        }
    }
    
    @Test
    public void testThreads() {
        Iterable<Long> threadIds = Profile.threads();
        assertNotNull(threadIds);
        Iterator<Long> iterator = threadIds.iterator();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testInteractions() {
        Iterable<Frame> interactions = Profile.interactions(123);
        assertNotNull(interactions);
        Iterator<Frame> iterator = interactions.iterator();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testFrameList() {
        Iterable<Frame> frameList = Profile.frameList();
        assertNotNull(frameList);
        Iterator<Frame> iterator = frameList.iterator();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testAllocations() {
        Iterable<ClassAllocation> allocations = Profile.allocations();
        assertNotNull(allocations);
        Iterator<ClassAllocation> iterator = allocations.iterator();
        assertFalse(iterator.hasNext());
    }
    
    @Test
    public void testGetThreadTotalTime() {
        long threadId = 123;
        long totalTime = Profile.getThreadTotalTime(threadId);
        assertEquals(0, totalTime);
    }
    
    @Test
    public void testSortFrameList() {
        Comparator<Frame> comp = new Comparator<Frame>() {
            public int compare(Frame f1, Frame f2) {
                return f1.getClassName().compareTo(f2.getClassName());
            }
        };
        Profile.sortFrameList(comp);
    }
    
    @Test
    public void testStartMethod() {
        String className = "com.example.Class";
        String methodName = "method";
        Profile.start(className, methodName);
        long threadId = Thread.currentThread().getId();
        Frame target = findFrame(threadId, className, methodName);
        assertNotNull(target);
        assertEquals(threadId, target.getThreadId());
        assertEquals(className, target.getClassName());
        assertEquals(methodName, target.getMethodName());
    }
    
    @Test
    public void testEndMethod() {
        String className = "com.example.Class";
        String methodName = "method";
        Profile.end(className, methodName);
        long threadId = Thread.currentThread().getId();
        Frame target = findFrame(threadId, className, methodName);
        assertNull(target);
    }
    
    @Test
    public void testBeginWait() {
        String className = "com.example.Class";
        String methodName = "method";
        Profile.beginWait(className, methodName);
        long threadId = Thread.currentThread().getId();
        Frame target = findFrame(threadId, className, methodName);
        assertNotNull(target);
        assertTrue(target.isWaiting());
    }
    
    @Test
    public void testEndWait() {
        String className = "com.example.Class";
        String methodName = "method";
        Profile.endWait(className, methodName);
        long threadId = Thread.currentThread().getId();
        Frame target = findFrame(threadId, className, methodName);
        assertNull(target);
    }
    
    @Test
    public void testUnwind() {
        String className = "com.example.Class";
        String methodName = "method";
        String exception = "java.lang.Exception";
        Profile.unwind(className, methodName, exception);
        long threadId = Thread.currentThread().getId();
        Frame target = findFrame(threadId, className, methodName);
        assertNotNull(target);
        assertEquals(threadId, target.getThreadId());
        assertEquals(className, target.getClassName());
        assertEquals(methodName, target.getMethodName());
    }
    
    @Test
    public void testAlloc() {
        String className = "com.example.Class";
        Profile.alloc(className);
        ClassAllocation ca = Profile._allocList.get(className);
        assertNotNull(ca);
        assertEquals(className, ca.getClassName());
        assertEquals(1, ca.getAllocCount());
    }
    
    private Frame findFrame(long threadId, String className, String methodName) {
        if (!Profile._controller.isProfileRunning()) {
            return null;
        }
        Frame target = Profile._threadActiveFrame.get(threadId);
        if (target == null) {
            return null;
        }
        while (!target.getClassName().equals(className) || !target.getMethodName().equals(methodName)) {
            target = target.getParent();
            if (target == null) {
                return null;
            }
        }
        return target;
    }
}