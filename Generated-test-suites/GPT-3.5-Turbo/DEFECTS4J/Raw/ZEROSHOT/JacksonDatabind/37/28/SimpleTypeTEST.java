import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleTypeTest {
    
    private SimpleType simpleType;
    
    @Before
    public void setUp() {
        simpleType = new SimpleType(String.class);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(String.class, simpleType.getRawClass());
        assertFalse(simpleType.isContainerType());
        assertNull(simpleType.getContentType());
        assertNotNull(simpleType.getTypeHandler());
        assertNotNull(simpleType.getValueHandler());
        assertTrue(simpleType.canDeserialize());
        assertTrue(simpleType.canSerialize());
        assertFalse(simpleType.isReferenceType());
        assertFalse(simpleType.isTypeOrSubTypeOf(SimpleType.class));
    }
    
    @Test
    public void testConstructUnsafe() {
        SimpleType constructedType = simpleType.constructUnsafe(String.class);
        assertNotEquals(simpleType, constructedType);
        assertEquals(String.class, constructedType.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeForMap() {
        simpleType.constructUnsafe(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeForCollection() {
        simpleType.constructUnsafe(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeForArray() {
        simpleType.constructUnsafe(String[].class);
    }
    
    @Test
    public void testConstruct() {
        SimpleType constructedType = simpleType.construct(String.class);
        assertNotEquals(simpleType, constructedType);
        assertEquals(String.class, constructedType.getRawClass());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForMap() {
        simpleType.construct(Map.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForCollection() {
        simpleType.construct(Collection.class);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructForArray() {
        simpleType.construct(String[].class);
    }
    
    @Test
    public void testNarrow() {
        SimpleType narrowedType = simpleType.narrow(String.class);
        assertNotEquals(simpleType, narrowedType);
        assertEquals(String.class, narrowedType.getRawClass());
    }
    
    @Test
    public void testWithContentType() {
        try {
            simpleType.withContentType(String.class);
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
    
    @Test
    public void testWithTypeHandler() {
        Object typeHandler = new Object();
        SimpleType typeWithHandler = simpleType.withTypeHandler(typeHandler);
        assertNotEquals(simpleType, typeWithHandler);
        assertEquals(typeHandler, typeWithHandler.getTypeHandler());
    }
    
    @Test
    public void testWithContentTypeHandler() {
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        SimpleType typeWithValueHandler = simpleType.withValueHandler(valueHandler);
        assertNotEquals(simpleType, typeWithValueHandler);
        assertEquals(valueHandler, typeWithValueHandler.getValueHandler());
    }
    
    @Test
    public void testWithContentValueHandler() {
        try {
            simpleType.withContentValueHandler(new Object());
            fail("Should have thrown an exception");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
    
    @Test
    public void testWithStaticTyping() {
        SimpleType typeWithStaticTyping = simpleType.withStaticTyping();
        assertNotEquals(simpleType, typeWithStaticTyping);
        assertTrue(typeWithStaticTyping.isStatic());
    }
    
    @Test
    public void testRefine() {
        assertNull(simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null));
    }
    
    @Test
    public void testBuildCanonicalName() {
        assertEquals("java.lang.String", simpleType.buildCanonicalName());
    }
    
    @Test
    public void testGetErasedSignature() {
        assertEquals("Ljava/lang/String;", simpleType.getErasedSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testGetGenericSignature() {
        assertEquals("Ljava/lang/String;;", simpleType.getGenericSignature(new StringBuilder()).toString());
    }
    
    @Test
    public void testToString() {
        assertEquals("[simple type, class java.lang.String]", simpleType.toString());
    }
    
    @Test
    public void testEquals() {
        assertFalse(simpleType.equals(null));
        assertFalse(simpleType.equals(""));
        assertTrue(simpleType.equals(simpleType));
        
        SimpleType sameType = new SimpleType(String.class);
        assertTrue(simpleType.equals(sameType));
        
        SimpleType differentType = new SimpleType(Integer.class);
        assertFalse(simpleType.equals(differentType));
        
        TypeBindings bindings = new TypeBindings();
        differentType = new SimpleType(String.class, bindings, null, null);
        assertFalse(simpleType.equals(differentType));
    }
}