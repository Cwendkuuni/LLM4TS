package com.mentorgen.tools.profile.runtime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.mentorgen.tools.profile.ClassAllocation;
import com.mentorgen.tools.profile.Frame;
import com.mentorgen.tools.profile.Method;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ProfileTest {
    
    @Before
    public void setUp() {
        Profile.initProfiler();
    }

    @After
    public void tearDown() {
        Profile.clear();
    }
    
    @Test
    public void testInitProfiler() {
        // Test initialization of profiler
        // ...
    }
    
    @Test
    public void testInit() {
        // Test initialization of profile attributes
        // ...
    }
    
    @Test
    public void testClear() {
        // Test clearing of profile attributes
        // ...
    }
    
    @Test
    public void testStart() {
        // Test starting of profiling
        // ...
    }
    
    @Test
    public void testStop() {
        // Test stopping of profiling
        // ...
    }
    
    @Test
    public void testSetFileName() {
        // Test setting file name for profile output
        // ...
    }
    
    @Test
    public void testShutdown() {
        // Test shutting down of profiling
        // ...
    }
    
    @Test
    public void testThreads() {
        // Test retrieving list of threads
        // ...
    }
    
    @Test
    public void testInteractions() {
        // Test retrieving list of interactions for a thread
        // ...
    }
    
    @Test
    public void testFrameList() {
        // Test retrieving list of frames
        // ...
    }
    
    @Test
    public void testAllocations() {
        // Test retrieving list of allocations
        // ...
    }
    
    @Test
    public void testGetThreadTotalTime() {
        // Test retrieving total time for a thread
        // ...
    }
    
    @Test
    public void testSortFrameList() {
        // Test sorting of frame list
        // ...
    }
    
    @Test
    public void testStart1() {
        // Test starting of a frame
        // ...
    }
    
    @Test
    public void testEnd() {
        // Test ending of a frame
        // ...
    }
    
    @Test
    public void testBeginWait() {
        // Test beginning of a wait
        // ...
    }
    
    @Test
    public void testEndWait() {
        // Test ending of a wait
        // ...
    }
    
    @Test
    public void testUnwind() {
        // Test unwinding of frames
        // ...
    }
    
    @Test
    public void testAlloc() {
        // Test allocation of class
        // ...
    }
    
    @Test
    public void testRun() throws IOException {
        // Test running of profiler thread
        // ...
    }
}