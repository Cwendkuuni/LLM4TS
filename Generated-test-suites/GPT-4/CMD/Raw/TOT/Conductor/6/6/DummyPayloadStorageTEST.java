

/*
 * Copyright 2022 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 */

package com.netflix.conductor.core.storage;

import com.netflix.conductor.common.run.ExternalStorageLocation;
import com.netflix.conductor.common.utils.ExternalPayloadStorage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;

/**
 * Tests for {@link DummyPayloadStorage}.
 */
public class DummyPayloadStorageTest {

    private DummyPayloadStorage dummyPayloadStorage;

    @Before
    public void setUp() {
        dummyPayloadStorage = new DummyPayloadStorage();
    }

    @Test
    public void testGetLocation() { 
        ExternalStorageLocation result = dummyPayloadStorage.getLocation(
            ExternalPayloadStorage.Operation.READ,
            ExternalPayloadStorage.PayloadType.TASK_INPUT,
            "dummyPath"
        );
        Assert.assertEquals(null, result);
    }

    @Test
    public void testUpload() { 
        try {
            dummyPayloadStorage.upload("dummyPath", null, 1024);
        } catch (Exception ex) {
            Assert.fail("Should not have thrown any exception" + ex);
        }
    }

    @Test
    public void testDownload() { 
        InputStream result = dummyPayloadStorage.download("dummyPath");
        Assert.assertEquals(null,result);
    }
}

