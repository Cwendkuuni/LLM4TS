import static org.junit.Assert.*;
import org.junit.Test;

public class TypeDeserializerBaseTest {

    @Test
    public void testBaseTypeName() {
        TypeDeserializerBase tdb = new TypeDeserializerBase(null, null, null, false, null) {};
        String typeName = tdb.baseTypeName();
        assertNotNull(typeName);
        assertEquals("java.lang.Object", typeName);
    }

    @Test
    public void testGetPropertyName() {
        TypeDeserializerBase tdb = new TypeDeserializerBase(null, null, "type", false, null) {};
        String propertyName = tdb.getPropertyName();
        assertNotNull(propertyName);
        assertEquals("type", propertyName);
    }

    @Test
    public void testGetTypeIdResolver() {
        TypeIdResolver idResolver = new TestTypeIdResolver();
        TypeDeserializerBase tdb = new TypeDeserializerBase(null, idResolver, null, false, null) {};
        TypeIdResolver resolver = tdb.getTypeIdResolver();
        assertNotNull(resolver);
        assertEquals(idResolver, resolver);
    }

    @Test
    public void testGetDefaultImpl() {
        TypeDeserializerBase tdb = new TypeDeserializerBase(null, null, null, false, String.class) {};
        Class<?> defaultImpl = tdb.getDefaultImpl();
        assertNotNull(defaultImpl);
        assertEquals(String.class, defaultImpl);
    }

    @Test
    public void testToString() {
        TypeDeserializerBase tdb = new TypeDeserializerBase(null, null, null, false, null) {};
        String expected = "[com.fasterxml.jackson.databind.jsontype.impl.TypeDeserializerBase; base-type:null; id-resolver: null]";
        assertEquals(expected, tdb.toString());
    }

    @Test
    public void testDeserializeWithNativeTypeId() {
        TestTypeIdResolver idResolver = new TestTypeIdResolver();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDefaultTyping(new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, idResolver));
        String json = "{\"type\":\"circle\",\"radius\":5}";
        
        try {
            Shape shape = mapper.readValue(json, Shape.class);
            assertNotNull(shape);
            assertEquals(Circle.class, shape.getClass());
            Circle circle = (Circle) shape;
            assertEquals(5, circle.getRadius());
        } catch(IOException e) {
            fail("IOException thrown.");
        }
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserializeWithUnknownTypeId() throws IOException {
        TestTypeIdResolver idResolver = new TestTypeIdResolver();
        ObjectMapper mapper = new ObjectMapper();
        mapper.setDefaultTyping(new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, idResolver));
        String json = "{\"type\":\"triangle\",\"base\":5,\"height\":10}";
        
        Shape shape = mapper.readValue(json, Shape.class);
    }

    private static abstract class Shape {
        public abstract String getType();
    }

    private static class Circle extends Shape {
        private int radius;

        public Circle(int radius) {
            this.radius = radius;
        }

        public int getRadius() {
            return radius;
        }

        @Override
        public String getType() {
            return "circle";
        }
    }

    private static class Triangle extends Shape {
        private int base;
        private int height;

        public Triangle(int base, int height) {
            this.base = base;
            this.height = height;
        }

        public int getBase() {
            return base;
        }

        public int getHeight() {
            return height;
        }

        @Override
        public String getType() {
            return "triangle";
        }
    }
}