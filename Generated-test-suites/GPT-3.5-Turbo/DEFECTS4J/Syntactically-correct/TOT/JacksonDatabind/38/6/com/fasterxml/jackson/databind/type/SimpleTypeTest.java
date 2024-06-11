import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor_withClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstructor_withClassAndBindings() {
        TypeBindings bindings = new TypeBindings();
        SimpleType simpleType = new SimpleType(Integer.class, bindings, null, null);
        assertEquals(Integer.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstructor_withTypeBase() {
        TypeBase base = new TypeBase(String.class);
        SimpleType simpleType = new SimpleType(base);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testConstructor_withAllParameters() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType(String.class);
        JavaType[] superInterfaces = new JavaType[]{new JavaType(Integer.class)};
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType simpleType = new SimpleType(Float.class, bindings, superClass, superInterfaces, valueHandler, typeHandler, true);
        assertEquals(Float.class, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertArrayEquals(superInterfaces, simpleType.getSuperInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertTrue(simpleType.isStatic());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(Double.class);
        assertEquals(Double.class, simpleType.getRawClass());
        assertNull(simpleType.getBindings());
        assertNull(simpleType.getSuperClass());
        assertNull(simpleType.getSuperInterfaces());
        assertNull(simpleType.getValueHandler());
        assertNull(simpleType.getTypeHandler());
        assertFalse(simpleType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_withMapClass() {
        SimpleType simpleType = SimpleType.construct(HashMap.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_withCollectionClass() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstruct_withArrayClass() {
        SimpleType simpleType = SimpleType.construct(Float[].class);
    }

    @Test
    public void testNarrow_withSubclass() {
        SimpleType simpleType = new SimpleType(Number.class);
        JavaType narrowedType = simpleType.narrow(Float.class);
        assertEquals(Float.class, narrowedType.getRawClass());
        assertNull(narrowedType.getBindings());
        assertEquals(simpleType, narrowedType.getSuperClass());
        assertNull(narrowedType.getSuperInterfaces());
        assertNull(narrowedType.getValueHandler());
        assertNull(narrowedType.getTypeHandler());
        assertFalse(narrowedType.isStatic());
    }

    @Test
    public void testWithContentType_withContentType() {
        SimpleType simpleType = new SimpleType(List.class);
        JavaType withContentTypeType = simpleType.withContentType(new JavaType(Object.class));
        assertEquals(List.class, withContentTypeType.getRawClass());
        assertNull(withContentTypeType.getBindings());
        assertNull(withContentTypeType.getSuperClass());
        assertNull(withContentTypeType.getSuperInterfaces());
        assertNull(withContentTypeType.getValueHandler());
        assertNull(withContentTypeType.getTypeHandler());
        assertFalse(withContentTypeType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType_withoutContentType() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType withContentTypeType = simpleType.withContentType(new JavaType(Object.class));
    }

    @Test
    public void testWithTypeHandler_withTypeHandler() {
        SimpleType simpleType = new SimpleType(Float.class);
        JavaType withTypeHandlerType = simpleType.withTypeHandler(new Object());
        assertEquals(Float.class, withTypeHandlerType.getRawClass());
        assertNull(withTypeHandlerType.getBindings());
        assertNull(withTypeHandlerType.getSuperClass());
        assertNull(withTypeHandlerType.getSuperInterfaces());
        assertNull(withTypeHandlerType.getValueHandler());
        assertNotNull(withTypeHandlerType.getTypeHandler());
        assertFalse(withTypeHandlerType.isStatic());
    }

    @Test
    public void testWithValueHandler_withValueHandler() {
        SimpleType simpleType = new SimpleType(Integer.class);
        JavaType withValueHandlerType = simpleType.withValueHandler(new Object());
        assertEquals(Integer.class, withValueHandlerType.getRawClass());
        assertNull(withValueHandlerType.getBindings());
        assertNull(withValueHandlerType.getSuperClass());
        assertNull(withValueHandlerType.getSuperInterfaces());
        assertNotNull(withValueHandlerType.getValueHandler());
        assertNull(withValueHandlerType.getTypeHandler());
        assertFalse(withValueHandlerType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(List.class);
        JavaType withContentValueHandlerType = simpleType.withContentValueHandler(new Object());
    }

    @Test
    public void testWithStaticTyping_withStaticTyping() {
        SimpleType simpleType = new SimpleType(Double.class);
        JavaType withStaticTypingType = simpleType.withStaticTyping();
        assertEquals(Double.class, withStaticTypingType.getRawClass());
        assertNull(withStaticTypingType.getBindings());
        assertNull(withStaticTypingType.getSuperClass());
        assertNull(withStaticTypingType.getSuperInterfaces());
        assertNull(withStaticTypingType.getValueHandler());
        assertNull(withStaticTypingType.getTypeHandler());
        assertTrue(withStaticTypingType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(List.class);
        JavaType refinedType = simpleType.refine(ArrayList.class, new TypeBindings(), null, new JavaType[]{new JavaType(Collection.class)});
        assertNull(refinedType);
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(Set.class);
        assertEquals("java.util.Set", simpleType.buildCanonicalName());
    }
    
    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(LinkedList.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Map.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        sb = simpleType.getErasedSignature(sb);
        expected.append("Ljava/util/Map;");
        assertEquals(expected.toString(), sb.toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Integer.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder expected = new StringBuilder();
        sb = simpleType.getGenericSignature(sb);
        expected.append("Ljava/lang/Integer;");
        assertEquals(expected.toString(), sb.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }

    @Test
    public void testEquals_withEqualObjects() {
        SimpleType simpleType1 = new SimpleType(List.class);
        SimpleType simpleType2 = new SimpleType(List.class);
        assertEquals(simpleType1, simpleType2);
    }

    @Test
    public void testEquals_withNotEqualObjects() {
        SimpleType simpleType1 = new SimpleType(Set.class);
        SimpleType simpleType2 = new SimpleType(List.class);
        assertNotEquals(simpleType1, simpleType2);
    }
}