package com.fasterxml.jackson.databind.ser.std;

import org.junit.Assert;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testSerialize() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), false);
        //TODO: Add test code
    }

    @Test
    public void testGetEnumValues() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), false);
        //TODO: Add test code
    }

    @Test
    public void testSerializeAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), false);
        //TODO: Add test code
    }

    @Test
    public void testIsShapeWrittenUsingIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), false);
        //TODO: Add test code
    }

}