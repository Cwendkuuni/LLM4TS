
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SimpleTypeTest {
    
    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
    }
    
    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(Integer.class);
        assertEquals(Integer.class, simpleType.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForMap() {
        SimpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForCollection() {
        SimpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForArray() {
        SimpleType.construct(Integer[].class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
        SimpleType narrowedType = simpleType.narrow(List.class);
        assertEquals(List.class, narrowedType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
        SimpleType newType = simpleType.withContentType(Integer.class);
        assertEquals(Integer.class, newType.getContentType().getRawClass());
    }
    
    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
        SimpleType newType = simpleType.withTypeHandler(new MyTypeHandler());
        assertEquals(MyTypeHandler.class, newType.getTypeHandler().getClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
        simpleType.withContentTypeHandler(new MyTypeHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
        SimpleType newType = simpleType.withContentValueHandler(new MyValueHandler());
        assertEquals(MyValueHandler.class, newType.getContentValueHandler().getClass());
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
        SimpleType newType = simpleType.withStaticTyping();
        assertEquals(true, newType.isStatic());
    }
    
    @Test
    public void testRefine() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
        JavaType refinedType = simpleType.refine(List.class, TypeBindings.emptyBindings(), null, null);
        assertEquals(null, refinedType);
    }
    
    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
        StringBuilder sb = new StringBuilder();
        simpleType.getGenericSignature(sb);
        assertEquals("Ljava/util/ArrayList<*>;", sb.toString());
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
        assertEquals(false, simpleType.isContainerType());
    }
    
    @Test
    public void testToString() {
        SimpleType simpleType = SimpleType.construct(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        SimpleType simpleType1 = SimpleType.construct(ArrayList.class);
        SimpleType simpleType2 = SimpleType.construct(ArrayList.class);
        assertEquals(true, simpleType1.equals(simpleType2));
    }
    
    private class MyTypeHandler {
    }
    
    private class MyValueHandler {
    }

}
