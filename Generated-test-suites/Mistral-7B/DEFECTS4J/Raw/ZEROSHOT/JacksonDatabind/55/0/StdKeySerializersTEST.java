package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class StdKeySerializersTest {

 @Test
 public void testGetStdKeySerializerWithNullRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, null, true);
 assertTrue(serializer instanceof StdKeySerializers.Dynamic);
 }

 @Test
 public void testGetStdKeySerializerWithStringRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, String.class, true);
 assertTrue(serializer instanceof StdKeySerializers.StringKeySerializer);
 }

 @Test
 public void testGetStdKeySerializerWithIntegerRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Integer.TYPE, true);
 assertTrue(serializer instanceof StdKeySerializers.Default);
 }

 @Test
 public void testGetStdKeySerializerWithDateRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Date.class, true);
 assertTrue(serializer instanceof StdKeySerializers.Default);
 }

 @Test
 public void testGetStdKeySerializerWithCalendarRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Calendar.class, true);
 assertTrue(serializer instanceof StdKeySerializers.Default);
 }

 @Test
 public void testGetStdKeySerializerWithUUIDRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, java.util.UUID.class, true);
 assertTrue(serializer instanceof StdKeySerializers.Default);
 }

 @Test
 public void testGetStdKeySerializerWithClassRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Class.class, true);
 assertTrue(serializer instanceof StdKeySerializers.Default);
 }

 @Test
 public void testGetStdKeySerializerWithEnumRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Enum.class, true);
 assertTrue(serializer instanceof StdKeySerializers.Dynamic);
 }

 @Test
 public void testGetStdKeySerializerWithCustomRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, MyCustomClass.class, true);
 assertNotNull(serializer);
 }

 @Test(expected = NullPointerException.class)
 public void testGetStdKeySerializerWithNullConfig() {
 StdKeySerializers.getStdKeySerializer(null, String.class, true);
 }

 @Test
 public void testGetFallbackKeySerializerWithNullRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, null);
 assertNull(serializer);
 }

 @Test
 public void testGetFallbackKeySerializerWithStringRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, String.class);
 assertTrue(serializer instanceof StdKeySerializers.StringKeySerializer);
 }

 @Test
 public void testGetFallbackKeySerializerWithIntegerRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Integer.TYPE);
 assertTrue(serializer instanceof StdKeySerializers.Default);
 }

 @Test
 public void testGetFallbackKeySerializerWithDateRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Date.class);
 assertTrue(serializer instanceof StdKeySerializers.Default);
 }

 @Test
 public void testGetFallbackKeySerializerWithCalendarRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Calendar.class);
 assertTrue(serializer instanceof StdKeySerializers.Default);
 }

 @Test
 public void testGetFallbackKeySerializerWithUUIDRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, java.util.UUID.class);
 assertTrue(serializer instanceof StdKeySerializers.Default);
 }

 @Test
 public void testGetFallbackKeySerializerWithClassRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Class.class);
 assertTrue(serializer instanceof StdKeySerializers.Default);
 }

 @Test
 public void testGetFallbackKeySerializerWithEnumRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, Enum.class);
 assertTrue(serializer instanceof StdKeySerializers.Dynamic);
 }

 @Test
 public void testGetFallbackKeySerializerWithCustomRawKeyType() {
 JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(null, MyCustomClass.class);
 assertNull(serializer);
 }

 @Test
 public void testGetDefault() {
 JsonSerializer<Object> serializer = StdKeySerializers.getDefault();
 assertTrue(serializer instanceof StdKeySerializers.Default);
 }
}