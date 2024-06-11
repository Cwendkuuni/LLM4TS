package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

public class EnumSerializerTest {
    
    @Test
    public void testSerialize() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), null);
        TestEnum testEnum = TestEnum.VALUE1;
        Assert.assertEquals("VALUE1", enumSerializer.serialize(testEnum, null, null));
    }
    
    @Test
    public void testGetEnumValues() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), null);
        EnumValues enumValues = enumSerializer.getEnumValues();
        Assert.assertNotNull(enumValues);
    }
    
    @Test
    public void testGetSchema() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), null);
        Object schema = enumSerializer.getSchema(null, null);
        Assert.assertNotNull(schema);
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, TestEnum.class), null);
        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = Mockito.mock(JavaType.class);
        enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        Mockito.verify(visitor).expectStringFormat(typeHint);
    }
    
    public enum TestEnum {
        VALUE1, VALUE2, VALUE3;
    }
}