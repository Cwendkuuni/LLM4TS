package com.fasterxml.jackson.databind.ser.test;

import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.databind.introspect.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import com.fasterxml.jackson.databind.ser.*;
import org.junit.Test;

public class PropertyBuilderTest {

 private static class TestBean {
 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 private int i;
 @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 private String s;
 private List<String> list;
 }

 @Test
 public void testPropertyBuilder() throws Exception {
 SerializationConfig config = new SerializationConfig();
 BeanDescription beanDesc = new BeanDescription(TestBean.class);
 PropertyBuilder pb = new PropertyBuilder(config, beanDesc);

 // test defaults
 assertNotNull(pb.getDefaultBean());
 assertNull(pb.getDefaultValue(JavaType.getType(int.class)));
 assertNull(pb.getDefaultValue(JavaType.getType(String.class)));
 assertNull(pb.getDefaultValue(JavaType.getType(TestBean.class)));

 // test custom inclusion
 assertNotNull(pb.getDefaultBean());
 assertEquals("", pb.getPropertyDefaultValue("s", null, JavaType.getType(String.class)));
 assertNull(pb.getPropertyDefaultValue("i", null, JavaType.getType(int.class)));
 assertNull(pb.getPropertyDefaultValue("list", null, JavaType.getType(List.class)));

 // test unsupported access
 assertNull(pb.getPropertyDefaultValue("i", new AnnotatedMember() {
 @Override
 public Object getValue(Object obj) throws Exception {
 throw new UnsupportedOperationException();
 }
 }, JavaType.getType(int.class)));

 // test null return
 assertNull(pb.getPropertyDefaultValue("list", null, JavaType.getType(List.class)));

 // test custom inclusion with default bean
 Object defaultBean = pb.getDefaultBean();
 assertNotNull(defaultBean);
 assertNull(pb.getPropertyDefaultValue("i", null, JavaType.getType(int.class), defaultBean));
 assertEquals(0, pb.getPropertyDefaultValue("i", null, JavaType.getType(int.class), defaultBean).intValue());
 assertNull(pb.getPropertyDefaultValue("s", null, JavaType.getType(String.class), defaultBean));
 assertNull(pb.getPropertyDefaultValue("list", null, JavaType.getType(List.class), defaultBean));
 }
}