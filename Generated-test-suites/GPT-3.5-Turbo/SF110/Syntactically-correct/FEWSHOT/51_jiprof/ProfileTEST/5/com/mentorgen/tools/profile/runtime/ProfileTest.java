package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import com.mentorgen.tools.profile.output.*;

public class ProfileTest {
    
    @Before
    public void setUp() {
        Profile.init();
    }
    
    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        // assert statements
        assertNotNull(Profile._controller);
        assertNotNull(Profile._controllerThread);
    }
    
    @Test
    public void testInit() {
        Profile.init();
        // assert statements
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
        // assert statements
        assertEquals(0, ((HashMap<Long, Frame>)Profile._threadActiveFrame).size());
        assertEquals(0, Profile._threadDictionary.size());
        assertEquals(0, Profile._methodDictionary.size());
        assertEquals(0, Profile._frameList.size());
        assertEquals(0, Profile._allocList.size());
    }
    
    @Test
    public void testStart() {
        Profile.start();
        // assert statements
        assertTrue(Profile._controller.isRunning());
    }
    
    @Test
    public void testStop() {
        Profile.stop();
        // assert statements
        assertFalse(Profile._controller.isRunning());
    }
    
    @Test
    public void testSetFileName() {
        String fileName = "test.txt";
        Profile.setFileName(fileName);
        // assert statements
        assertEquals(fileName, Profile._controller.getFileName());
    }
    
    @Test
    public void testShutdown() {
        Profile.shutdown();
        // assert statements
        assertFalse(Controller._profile);
        assertEquals(0, ((HashMap<Long, V>)Profile._threadDictionary).size());
        assertEquals(0, Profile._frameList.size());
    }
    
    @Test
    public void testThreads() {
        Iterable<Long> threads = Profile.threads();
        // assert statements
        assertNotNull(threads);
    }
    
    @Test
    public void testInteractions() {
        long threadId = 12345;
        Iterable<Frame> interactions = Profile.interactions(threadId);
        // assert statements
        assertNotNull(interactions);
    }
    
    @Test
    public void testFrameList() {
        Iterable<Frame> frameList = Profile.frameList();
        // assert statements
        assertNotNull(frameList);
    }
    
    @Test
    public void testAllocations() {
        Iterable<ClassAllocation> allocations = Profile.allocations();
        // assert statements
        assertNotNull(allocations);
    }
    
    @Test
    public void testGetThreadTotalTime() {
        long threadId = 12345;
        long totalTime = Profile.getThreadTotalTime(threadId);
        // assert statements
        assertEquals(0, totalTime);
    }
    
    @Test
    public void testSortFrameList() {
        Comparator<Frame> comp = new Comparator<Frame>() {
            public int compare(Frame f1, Frame f2) {
                return f1.getMethodName().compareTo(f2.getMethodName());
            }
        };
        Profile.sortFrameList(comp);
        // assert statements
        // add assertions here
    }
    
    @Test
    public void testStartWithClassNameAndMethodName() {
        String className = "com.example.ExampleClass";
        String methodName = "exampleMethod";
        Profile.start(className, methodName);
        // assert statements
        // add assertions here
    }
    
    @Test
    public void testEndWithClassNameAndMethodName() {
        String className = "com.example.ExampleClass";
        String methodName = "exampleMethod";
        Profile.end(className, methodName);
        // assert statements
        // add assertions here
    }
    
    @Test
    public void testBeginWaitWithClassNameAndMethodName() {
        String className = "com.example.ExampleClass";
        String methodName = "exampleMethod";
        Profile.beginWait(className, methodName);
        // assert statements
        // add assertions here
    }
    
    @Test
    public void testEndWaitWithClassNameAndMethodName() {
        String className = "com.example.ExampleClass";
        String methodName = "exampleMethod";
        Profile.endWait(className, methodName);
        // assert statements
        // add assertions here
    }
    
    @Test
    public void testUnwindWithClassNameAndMethodNameAndException() {
        String className = "com.example.ExampleClass";
        String methodName = "exampleMethod";
        String exception = "java.lang.Exception";
        Profile.unwind(className, methodName, exception);
        // assert statements
        // add assertions here
    }
    
    @Test
    public void testAllocWithClassName() {
        String className = "com.example.ExampleClass";
        Profile.alloc(className);
        // assert statements
        // add assertions here
    }
    
    @Test
    public void testRun() {
        Profile.run();
        // assert statements
        // add assertions here
    }
}