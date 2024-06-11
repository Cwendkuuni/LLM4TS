
package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ProfileTest {
    
    private List<Frame> frameList;
    private Map<Long, Frame> threadActiveFrame;
    private Map<String, Method> methodDictionary;
    private Object lock;
    private Map<String, ClassAllocation> allocList;
    private Controller controller;
    
    @Before
    public void setUp() {
        Profile.init();
        
        frameList = new ArrayList<Frame>();
        threadActiveFrame = new HashMap<Long, Frame>(1001);
        methodDictionary = new HashMap<String, Method>(2003);
        lock = new Object();
        allocList = new HashMap<String, ClassAllocation>();
        controller = new Controller();
    }
    
    @Test
    public void testInitProfiler() {
        Profile.initProfiler();
        // TODO: Add assertions here
    }
    
    @Test
    public void testInit() {
        Profile.init();
        // TODO: Add assertions here
    }
    
    @Test
    public void testClear() {
        Profile.clear();
        // TODO: Add assertions here
    }
    
    @Test
    public void testStart() {
        Profile.start();
        // TODO: Add assertions here
    }
    
    @Test
    public void testStop() {
        Profile.stop();
        // TODO: Add assertions here
    }
    
    @Test
    public void testSetFileName() {
        Profile.setFileName("file.txt");
        // TODO: Add assertions here
    }
    
    @Test
    public void testShutdown() {
        Profile.shutdown();
        // TODO: Add assertions here
    }
    
    @Test
    public void testThreads() {
        Iterable<Long> threads = Profile.threads();
        // TODO: Add assertions here
    }
    
    @Test
    public void testInteractions() {
        Iterable<Frame> interactions = Profile.interactions(1);
        // TODO: Add assertions here
    }
    
    @Test
    public void testFrameList() {
        Iterable<Frame> frames = Profile.frameList();
        // TODO: Add assertions here
    }
    
    @Test
    public void testAllocations() {
        Iterable<ClassAllocation> allocations = Profile.allocations();
        // TODO: Add assertions here
    }
    
    @Test
    public void testGetThreadTotalTime() {
        long totalTime = Profile.getThreadTotalTime(1);
        // TODO: Add assertions here
    }
    
    @Test
    public void testSortFrameList() {
        Comparator<Frame> comp = new Comparator<Frame>() {
            public int compare(Frame f1, Frame f2) {
                return f1.getName().compareTo(f2.getName());
            }
        };
        Profile.sortFrameList(comp);
        // TODO: Add assertions here
    }
    
    @Test
    public void testStartMethod() {
        Profile.start("com.example.ExampleClass", "exampleMethod");
        // TODO: Add assertions here
    }
    
    @Test
    public void testEndMethod() {
        Profile.end("com.example.ExampleClass", "exampleMethod");
        // TODO: Add assertions here
    }
    
    @Test
    public void testBeginWait() {
        Profile.beginWait("com.example.ExampleClass", "exampleMethod");
        // TODO: Add assertions here
    }
    
    @Test
    public void testEndWait() {
        Profile.endWait("com.example.ExampleClass", "exampleMethod");
        // TODO: Add assertions here
    }
    
    @Test
    public void testUnwind() {
        Profile.unwind("com.example.ExampleClass", "exampleMethod", "Exception");
        // TODO: Add assertions here
    }
    
    @Test
    public void testAlloc() {
        Profile.alloc("com.example.ExampleClass");
        // TODO: Add assertions here
    }
    
    @Test
    public void testRun() {
        Profile profile = new Profile();
        profile.run();
        // TODO: Add assertions here
    }
    
}
