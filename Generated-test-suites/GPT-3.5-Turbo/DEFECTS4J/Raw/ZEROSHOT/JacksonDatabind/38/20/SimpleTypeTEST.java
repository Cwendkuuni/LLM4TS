import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleTypeTest {
    private SimpleType simpleType;
  
    @Before
    public void setUp() {
        simpleType = new SimpleType(String.class);
    }
  
    @Test
    public void testConstructUnsafe() {
        SimpleType result = SimpleType.constructUnsafe(String.class);
        assertNotNull(result);
    }
  
    @Test
    public void testConstruct() {
        try {
            SimpleType result = SimpleType.construct(Map.class);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }
      
        try {
            SimpleType result = SimpleType.construct(Collection.class);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }
      
        try {
            SimpleType result = SimpleType.construct(String[].class);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
      
        SimpleType result = SimpleType.construct(String.class);
        assertNotNull(result);
    }
  
    @Test
    public void testWithContentType() {
        try {
            simpleType.withContentType(new SimpleType(String.class));
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }
  
    @Test
    public void testWithTypeHandler() {
        Object handler = new Object();
        SimpleType result = simpleType.withTypeHandler(handler);
        assertSame(handler, result.getTypeHandler());
    }
  
    @Test
    public void testWithContentTypeHandler() {
        try {
            simpleType.withContentTypeHandler(new Object());
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }
  
    @Test
    public void testWithValueHandler() {
        Object handler = new Object();
        SimpleType result = simpleType.withValueHandler(handler);
        assertSame(handler, result.getValueHandler());
    }
  
    @Test
    public void testWithContenValueHandler() {
        try {
            simpleType.withContentValueHandler(new Object());
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }
  
    @Test
    public void testWithStaticTyping() {
        SimpleType result = simpleType.withStaticTyping();
        assertTrue(result.isStaticTyping());
    }
  
    @Test
    public void testRefine() {
        JavaType result = simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null);
        assertNull(result);
    }
  
    @Test
    public void testIsContainerType() {
        assertFalse(simpleType.isContainerType());
    }
  
    @Test
    public void testGetErasedSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getErasedSignature(sb);
        assertSame(sb, result);
    }
  
    @Test
    public void testGetGenericSignature() {
        StringBuilder sb = new StringBuilder();
        StringBuilder result = simpleType.getGenericSignature(sb);
        assertSame(sb, result);
    }
}