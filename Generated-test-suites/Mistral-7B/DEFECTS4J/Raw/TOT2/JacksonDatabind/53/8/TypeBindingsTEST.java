import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TypeBindingsTest {

    @Test
    public void testEmptyBindings() {
        TypeBindings emptyBindings = TypeBindings.emptyBindings();
        assertEquals("", emptyBindings.toString());
        assertEquals(0, emptyBindings.size());
    }

    @Test
    public void testCreateWithEmptyTypeList() {
        String className = "java.util.List";
        TypeBindings typeBindings = TypeBindings.create(Class.forName(className), null);
        assertEquals(className, typeBindings.toString());
        assertEquals(0, typeBindings.size());
    }

    @Test
    public void testCreateWithOneType() {
        String className = "java.util.List";
        String typeName = "java.lang.Integer";
        JavaType javaType = TypeFactory.defaultInstance().constructType(typeName);
        TypeBindings typeBindings = TypeBindings.create(Class.forName(className), javaType);
        assertEquals(className, typeBindings.toString());
        assertEquals(1, typeBindings.size());
        assertEquals(typeName, typeBindings.getBoundName(0));
        assertEquals(javaType, typeBindings.getBoundType(0));
    }

    @Test
    public void testCreateWithTwoTypes() {
        String className = "java.util.Map<java.lang.String, java.lang.Integer>";
        JavaType javaType1 = TypeFactory.defaultInstance().constructType("java.lang.String");
        JavaType javaType2 = TypeFactory.defaultInstance().constructType("java.lang.Integer");
        TypeBindings typeBindings = TypeBindings.create(Class.forName(className), javaType1, javaType2);
        assertEquals(className, typeBindings.toString());
        assertEquals(2, typeBindings.size());
        assertEquals("java.lang.String", typeBindings.getBoundName(0));
        assertEquals(javaType1, typeBindings.getBoundType(0));
        assertEquals("java.lang.Integer", typeBindings.getBoundName(1));
        assertEquals(javaType2, typeBindings.getBoundType(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMismatchedTypes() {
        String className = "java.util.Map<java.lang.String, java.lang.Integer>";
        String typeName = "java.lang.Long";
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(typeName);
        TypeBindings typeBindings = TypeBindings.create(Class.forName(className), javaType1);
        assertNotNull(typeBindings);
        // This should throw an IllegalArgumentException
    }

    @Test
    public void testCreateIfNeededWithEmptyTypeList() {
        String className = "java.util.List";
        TypeBindings typeBindings = TypeBindings.createIfNeeded(Class.forName(className), null);
        assertEquals("", typeBindings.toString());
        assertEquals(0, typeBindings.size());
    }

    @Test
    public void testCreateIfNeededWithOneType() {
        String className = "java.util.List<java.lang.Integer>";
        JavaType javaType = TypeFactory.defaultInstance().constructType("java.lang.Integer");
        TypeBindings typeBindings = TypeBindings.createIfNeeded(Class.forName(className), javaType);
        assertEquals(className, typeBindings.toString());
        assertEquals(1, typeBindings.size());
        assertEquals("java.lang.Integer", typeBindings.getBoundName(0));
        assertEquals(javaType, typeBindings.getBoundType(0));
    }

    @Test
    public void testCreateIfNeededWithTwoTypes() {
        String className = "java.util.Map<java.lang.String, java.lang.Integer>";
        JavaType javaType1 = TypeFactory.defaultInstance().constructType("java.lang.String");
        JavaType javaType2 = TypeFactory.defaultInstance().constructType("java.lang.Integer");
        TypeBindings typeBindings = TypeBindings.createIfNeeded(Class.forName(className), javaType1, javaType2);
        assertEquals(className, typeBindings.toString());
        assertEquals(2, typeBindings.size());
        assertEquals("java.lang.String", typeBindings.getBoundName(0));
        assertEquals(javaType1, typeBindings.getBoundType(0));
        assertEquals("java.lang.Integer", typeBindings.getBoundName(1));
        assertEquals(javaType2, typeBindings.getBoundType(1));
    }

    @Test
    public void testFindBoundType() {
        String className = "java.util.Map<java.lang.String, java.lang.Integer>";
        JavaType javaType1 = TypeFactory.defaultInstance().constructType("java.lang.String");
        JavaType javaType2 = TypeFactory.defaultInstance().constructType("java.lang.Integer");
        TypeBindings typeBindings = TypeBindings.createIfNeeded(Class.forName(className), javaType1, javaType2);
        assertEquals("java.lang.String", typeBindings.getBoundName(0));
        assertEquals("java.lang.Integer", typeBindings.getBoundName(1));
        assertEquals(javaType1, typeBindings.findBoundType("java.lang.String"));
        assertEquals(javaType2, typeBindings.findBoundType("java.lang.Integer"));
        assertEquals(null, typeBindings.findBoundType("java.lang.Boolean"));
    }

    @Test
    public void testIsEmpty() {
        TypeBindings emptyBindings = TypeBindings.emptyBindings();
        assertTrue(emptyBindings.isEmpty());

        String className = "java.util.List";
        JavaType javaType = TypeFactory.defaultInstance().constructType("java.lang.Integer");
        TypeBindings typeBindings = TypeBindings.create(Class.forName(className), javaType);
        assertFalse(typeBindings.isEmpty());
    }

    @Test
    public void testSize() {
        TypeBindings emptyBindings = TypeBindings.emptyBindings();
        assertEquals(0, emptyBindings.size());

        String className = "java.util.List<java.lang.Integer>";
        JavaType javaType = TypeFactory.defaultInstance().constructType("java.lang.Integer");
        TypeBindings typeBindings = TypeBindings.create(Class.forName(className), javaType);
        assertEquals(1, typeBindings.size());

        className = "java.util.Map<java.lang.String, java.lang.Integer>";
        javaType1 = TypeFactory.defaultInstance().constructType("java.lang.String");
        javaType2 = TypeFactory.defaultInstance().constructType("java.lang.Integer");
        TypeBindings mapTypeBindings = TypeBindings.createIfNeeded(Class.forName(className), javaType1, javaType2);
        assertEquals(2, mapTypeBindings.size());
    }

    @Test
    public void testGetBoundName() {
        String className = "java.util.Map<java.lang.String, java.lang.Integer>";
        JavaType javaType1 = TypeFactory.defaultInstance().constructType("java.lang.String");
        JavaType javaType2 = TypeFactory.defaultInstance().constructType("java.lang.Integer");
        TypeBindings typeBindings = TypeBindings.createIfNeeded(Class.forName(className), javaType1, javaType2);
        assertEquals("java.lang.String", typeBindings.getBoundName(0));
        assertEquals("java.lang.Integer", typeBindings.getBoundName(1));
    }

    @Test
    public void testGetBoundType() {
        String className = "java.util.Map<java.lang.String, java.lang.Integer>";
        JavaType javaType1 = TypeFactory.defaultInstance().constructType("java.lang.String");
        JavaType javaType2 = TypeFactory.defaultInstance().constructType("java.lang.Integer");
        TypeBindings typeBindings = TypeBindings.createIfNeeded(Class.forName(className), javaType1, javaType2);
        assertEquals(javaType1, typeBindings.getBoundType(0));
        assertEquals(javaType2, typeBindings.getBoundType(1));
    }

    @Test
    public void testGetTypeParameters() {
        String className = "java.util.Map<java.lang.String, java.lang.Integer>";
        JavaType javaType1 = TypeFactory.defaultInstance().constructType("java.lang.String");
        JavaType javaType2 = TypeFactory.defaultInstance().constructType("java.lang.Integer");
        TypeBindings typeBindings = TypeBindings.createIfNeeded(Class.forName(className), javaType1, javaType2);
        List<JavaType> typeParameters = typeBindings.getTypeParameters();
        assertEquals(javaType1, typeParameters.get(0));
        assertEquals(javaType2, typeParameters.get(1));
    }

    @Test
    public void testHasUnbound() {
        String className = "java.util.Map<java.lang.String, java.lang.Integer>";
        JavaType javaType1 = TypeFactory.defaultInstance().constructType("java.lang.String");
        JavaType javaType2 = TypeFactory.defaultInstance().constructType("java.lang.Integer");
        TypeBindings typeBindings = TypeBindings.createIfNeeded(Class.forName(className), javaType1, javaType2);
        assertFalse(typeBindings.hasUnbound("java.lang.String"));
        assertFalse(typeBindings.hasUnbound("java.lang.Integer"));

        String unboundName = "java.util.UknownType";
        TypeBindings unboundTypeBindings = typeBindings.withUnboundVariable(unboundName);
        assertTrue(unboundTypeBindings.hasUnbound(unboundName));
        assertFalse(unboundTypeBindings.hasUnbound("java.lang.String"));
        assertFalse(unboundTypeBindings.hasUnbound("java.lang.Integer"));
    }

    @Test
    public void testToString() {
        String className = "java.util.Map<java.lang.String, java.lang.Integer>";
        JavaType javaType1 = TypeFactory.defaultInstance().constructType("java.lang.String");
        JavaType javaType2 = TypeFactory.defaultInstance().constructType("java.lang.Integer");
        TypeBindings typeBindings = TypeBindings.createIfNeeded(Class.forName(className), javaType1, javaType2);
        assertEquals(className, typeBindings.toString());

        className = "java.util.List<java.lang.Integer>";
        JavaType javaType = TypeFactory.defaultInstance().constructType("java.lang.Integer");
        typeBindings = TypeBindings.create(Class.forName(className), javaType);
        assertEquals(className, typeBindings.toString());
    }
}