package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializer;
import com.fasterxml.jackson.databind.util.StdDateFormat;

public class StdKeySerializerTest {
    
    @Test
    public void testSerialize_Date() {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = null;
        SerializerProvider provider = null;
        Date date = new Date();
        
        try {
            serializer.serialize(date, jgen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //assert something
    }
    
    @Test
    public void testSerialize_String() {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = null;
        SerializerProvider provider = null;
        String str = "test";
        
        try {
            serializer.serialize(str, jgen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //assert something
    }
    
    @Test
    public void testSerialize_Other() {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonGenerator jgen = null;
        SerializerProvider provider = null;
        Object obj = new Object();
        
        try {
            serializer.serialize(obj, jgen, provider);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //assert something
    }

    @Test
    public void testGetSchema() {
        StdKeySerializer serializer = new StdKeySerializer();
        SerializerProvider provider = null;
        Object obj = new Object();
        
        try {
            JsonNode schema = serializer.getSchema(provider, obj.getClass());
            
            //assert something
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        StdKeySerializer serializer = new StdKeySerializer();
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            
            //assert something
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}