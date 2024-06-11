package com.oceanbase.odc.plugin.task.api.datatransfer.model;
/*
 * Copyright (c) 2023 OceanBase.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * ...
 */

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class ObjectResultTest {
    private ObjectResult objectResult;

    @Before
    public void setup() {
        objectResult = new ObjectResult("schema", "name", "type");
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals("schema", objectResult.getSchema());
        Assert.assertEquals("name", objectResult.getName());
        Assert.assertEquals("type", objectResult.getType());
        Assert.assertEquals(0, objectResult.getCount().get());
        Assert.assertEquals(0, objectResult.getTotal().get());
    }

    @Test
    public void testOfMethod() {
        ObjectStatus objectStatus = new ObjectStatus("name", new AtomicLong(10), new AtomicLong(20), "status");
        objectStatus.setSchema("schema");
        objectStatus.setType("type");
        ObjectResult result = ObjectResult.of(objectStatus);
        Assert.assertEquals(objectStatus.getSchema(), result.getSchema());
        Assert.assertEquals(objectStatus.getName(), result.getName());
        Assert.assertEquals(objectStatus.getType(), result.getType());
        Assert.assertEquals(objectStatus.getCount().get(), result.getCount().get());
        Assert.assertEquals(objectStatus.getTotal().get(), result.getTotal().get());
    }

    @Test
    public void testEqualsAndHashCodeMethods() {
        ObjectResult other = new ObjectResult("schema", "name", "type");
        Assert.assertEquals(objectResult, other);
        Assert.assertEquals(objectResult.hashCode(), other.hashCode());
    }
    
    @Test
    public void testToStringMethod() {
        Assert.assertEquals("ObjectResult(super=ObjectStatus(name=name, count=0, total=0, status=null), exportPaths=null, schema=schema, type=type)", objectResult.toString());
    }

    @Test
    public void testGetSummary() {
        Assert.assertEquals("schema.name[type]", objectResult.getSummary());
    }
}