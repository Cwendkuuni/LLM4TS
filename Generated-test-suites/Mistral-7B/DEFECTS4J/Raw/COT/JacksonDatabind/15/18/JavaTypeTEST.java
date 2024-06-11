import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.TypeFactory;

public class JavaTypeTest {

    

    @Test
    public void testWithTypeHandler() {
        JavaType originalType = typeFactory.constructType(String.class);
        JavaType newType = originalType.withTypeHandler(new Object());
        assertEquals(originalType.getClass(), newType.getClass());
        assertNotSame(originalType.getValueHandler(), newType.getValueHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType originalType = typeFactory.constructType(List.class);
        JavaType newType = originalType.withContentTypeHandler(new Object());
        assertEquals(originalType.getClass(), newType.getClass());
        assertNotSame(originalType.getTypeHandler(), newType.getTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType originalType = typeFactory.constructType(String.class);
        JavaType newType = originalType.withValueHandler(new Object());
        assertEquals(originalType.getClass(), newType.getClass());
        assertNotSame(originalType.getValueHandler(), newType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType originalType = typeFactory.constructType(List.class);
        JavaType newType = originalType.withContentValueHandler(new Object());
        assertEquals(originalType.getClass(), newType.getClass());
        assertNotSame(originalType.getContentValueHandler(), newType.getContentValueHandler());
    }

    @Test
    public void testNarrowBy() {
        JavaType superClassType = typeFactory.constructType(Object.class);
        JavaType subClassType = typeFactory.constructType(String.class);

        JavaType result = superClassType.narrowBy(String.class);
        assertEquals(subClassType.getClass(), result.getClass());
        assertEquals(subClassType.getValueHandler(), result.getValueHandler());
        assertEquals(subClassType.getTypeHandler(), result.getTypeHandler());
    }

    @Test
    public void testWidenBy() {
        JavaType subClassType = typeFactory.constructType(String.class);
        JavaType superClassType = typeFactory.constructType(Object.class);

        JavaType result = subClassType.widenBy(Object.class);
        assertEquals(superClassType.getClass(), result.getClass());
        assertEquals(result.getValueHandler(), subClassType.getValueHandler());
        assertEquals(result.getTypeHandler(), subClassType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowByInvalid() {
        JavaType superClassType = typeFactory.constructType(Object.class);
        JavaType subClassType = typeFactory.constructType(Integer.class);

        superClassType.narrowBy(String.class);
    }

    @Test
    public void testHasRawClass() {
        JavaType type = typeFactory.constructType(String.class);
        assertTrue(type.hasRawClass(String.class));
    }

    @Test
    public void testIsAbstract() {
        JavaType abstractType = typeFactory.constructType(AbstractClass.class);
        assertTrue(abstractType.isAbstract());
    }

    @Test
    public void testIsConcrete() {
        JavaType concreteType = typeFactory.constructType(ConcreteClass.class);
        assertTrue(concreteType.isConcrete());
    }

    @Test
    public void testIsEnumType() {
        JavaType enumType = typeFactory.constructType(Enum.class);
        assertTrue(enumType.isEnumType());
    }

    @Test
    public void testIsInterface() {
        JavaType interfaceType = typeFactory.constructType(Interface.class);
        assertTrue(interfaceType.isInterface());
    }

    @Test
    public void testIsPrimitive() {
        JavaType primitiveType = typeFactory.constructType(int.class);
        assertTrue(primitiveType.isPrimitive());
    }

    @Test
    public void testIsFinal() {
        JavaType finalType = typeFactory.constructType(FinalClass.class);
        assertTrue(finalType.isFinal());
    }

    @Test
    public void testIsContainerType() {
        JavaType listType = typeFactory.constructType(List.class);
        assertTrue(listType.isContainerType());
    }

    @Test
    public void testIsCollectionLikeType() {
        JavaType listType = typeFactory.constructType(List.class);
        assertFalse(listType.isCollectionLikeType());
    }

    @Test
    public void testIsMapLikeType() {
        JavaType mapType = typeFactory.constructType(Map.class);
        assertFalse(mapType.isMapLikeType());
    }

    @Test
    public void testUseStaticType() {
        JavaType staticType = typeFactory.constructType(Boolean.TYPE);
        assertTrue(staticType.useStaticType());

        JavaType dynamicType = typeFactory.constructType(Boolean[].class);
        assertFalse(dynamicType.useStaticType());
    }

    @Test
    public void testHasGenericTypes() {
        JavaType listType = typeFactory.constructType(List.class);
        assertTrue(listType.hasGenericTypes());
    }

    @Test
    public void testGetKeyType() {
        JavaType mapType = typeFactory.constructType(Map.class);
        assertNull(mapType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType listType = typeFactory.constructType(List.class);
        assertNull(listType.getContentType());
    }

    @Test
    public void testContainedTypeCount() {
        JavaType mapType = typeFactory.constructType(Map.class);
        assertEquals(2, mapType.containedTypeCount());
    }

    @Test
    public void testContainedTypeOrUnknown() {
        JavaType mapType = typeFactory.constructType(Map.class);
        JavaType keyType = mapType.containedTypeOrUnknown(0);
        JavaType valueType = mapType.containedTypeOrUnknown(1);

        assertNotNull(keyType);
        assertNotNull(valueType);

        // Add your assertions for expected types here
    }

    @Test
    public void testGetGenericSignature() {
        JavaType listType = typeFactory.constructType(List.class);
        StringBuilder sb = new StringBuilder();
        listType.getGenericSignature(sb);
        assertEquals("java.util.List<E>", sb.toString());
    }

    @Test
    public void testGetErasedSignature() {
        JavaType listType = typeFactory.constructType(List.class);
        StringBuilder sb = new StringBuilder();
        listType.getErasedSignature(sb);
        assertEquals("java.util.List", sb.toString());
    }

    
}