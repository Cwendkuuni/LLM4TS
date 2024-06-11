package com.netflix.conductor.core.storage;

/*
* Copyright 2022 Your Company, Inc. All rights reserved.
* 
* This is a comprehensive set of test cases for the DummyPayloadStorage class.
*/
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.Operation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage.PayloadType;
import com.netflix.conductor.core.storage.DummyPayloadStorage;

import static org.junit.Assert.*;

import java.io.InputStream;

@RunWith(JUnit4.class)
public class DummyPayloadStorageTest {
    
    private DummyPayloadStorage dummyPayloadStorage;
    
    @Before
    public void setUp() {
        this.dummyPayloadStorage = new DummyPayloadStorage();
    }
    
    @Test
    public void testGetLocation() {
        // As getLocation() method returns null, any arguments will suffice
        ExternalStorageLocation location = dummyPayloadStorage.getLocation(Operation.READ, 
        PayloadType.TASK_INPUT, "path");

        assertNull("GetLocation should return null", location);
    }

    @Test
    public void testUpload() {
        //As upload does nothing in the DummyPayloadStorage, no actual test needed here
        // Just to check if method runs without errors
        dummyPayloadStorage.upload("path", null, 0);
    }

    @Test
    public void testDownload() {
        // As the download method always returns null, just a check to verify
        InputStream inputStream = dummyPayloadStorage.download("path");
        assertNull("Download should return null", inputStream);
    }
}
