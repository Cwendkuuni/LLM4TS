package com.fasterxml.jackson.databind.type;

import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

   @Test
   public void testConstructMapType() {
      MapType mapType = MapType.construct(HashMap.class, String.class, Integer.class);
      assertEquals("Should return HashMap class", HashMap.class, mapType.getRawClass());
      assertEquals("Should return key type as String", String.class, mapType.getKeyType());
      assertEquals("Should return value type as Integer", Integer.class, mapType.getValueType());
   }

   @Test
   public void testWithTypeHandler() {
      Object handler = new Object();
      MapType mapType = MapType.construct(HashMap.class, String.class, Integer.class);
      MapType mapTypeWithTypeHandler = mapType.withTypeHandler(handler);
      assertEquals("Should return handler object", handler, mapTypeWithTypeHandler.getTypeHandler());
   }

   @Test
   public void testWithStaticTyping() {
      MapType mapType = MapType.construct(HashMap.class, String.class, Integer.class);
      MapType mapTypeWithStaticTyping = mapType.withStaticTyping();
      assertTrue("Should have static typing", mapTypeWithStaticTyping.hasStaticTyping());
   }

   // Add more test cases for other methods as needed

}