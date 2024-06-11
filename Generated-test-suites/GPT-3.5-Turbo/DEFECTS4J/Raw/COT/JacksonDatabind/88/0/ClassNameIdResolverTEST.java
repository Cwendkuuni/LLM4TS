import org.junit.Assert;
import org.junit.Test;

public class ClassNameIdResolverTest {

    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Assert.assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // No test required as this method is not used
    }

    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValue(new Object());
        Assert.assertNotNull(id);
    }

    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValueAndType(new Object(), Object.class);
        Assert.assertNotNull(id);
    }

    @Test
    public void testTypeFromId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        try {
            resolver.typeFromId(null, "exampleType");
            Assert.fail("Expected IOException to be thrown");
        } catch (IOException e) {
            Assert.assertEquals("Invalid type id 'exampleType' (for id type 'Id.class'): null", e.getMessage());
        }
    }

    @Test
    public void test_typeFromId_with_unknown_id() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // simulate a DeserializationContext
        DatabindContext mockContext = new DeserializationContext(null, null, null);
        try {
            resolver._typeFromId("unknownId", mockContext);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid type id 'unknownId' (for id type 'Id.class'): no such class found", e.getMessage());
        }
    }

    @Test
    public void test_typeFromId_with_invalid_id() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // simulate a DeserializationContext
        DatabindContext mockContext = new DeserializationContext(null, null, null);
        try {
            resolver._typeFromId("invalidId", mockContext);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("Invalid type id 'invalidId' (for id type 'Id.class'): null", e.getMessage());
        }
    }

    @Test
    public void test_idFrom_with_enum_value() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String enumId = resolver._idFrom(TestEnum.VALUE, TestEnum.VALUE.getClass(), null);
        Assert.assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TestEnum", enumId);
        String subEnumId = resolver._idFrom(TestSubEnum.VALUE, TestSubEnum.VALUE.getClass(), null);
        Assert.assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TestSubEnum", subEnumId);
    }

    @Test
    public void test_idFrom_with_util_classes() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String enumSetId = resolver._idFrom(EnumSet.noneOf(TestEnum.class), EnumSet.noneOf(TestEnum.class).getClass(), null);
        Assert.assertEquals("java.util.EnumSet<com.fasterxml.jackson.databind.jsontype.impl.TestEnum>", enumSetId);
        String enumMapId = resolver._idFrom(new EnumMap<>(TestEnum.class), EnumMap.class, null);
        Assert.assertEquals("java.util.EnumMap<com.fasterxml.jackson.databind.jsontype.impl.TestEnum,java.lang.Object>", enumMapId);
        String arrayListId = resolver._idFrom(new ArrayList<>(), ArrayList.class, null);
        Assert.assertEquals("java.util.ArrayList", arrayListId);
    }

    @Test
    public void test_idFrom_with_inner_classes() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String innerClassId = resolver._idFrom(new InnerClass(), InnerClass.class, null);
        Assert.assertEquals("com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolverTest$InnerClass", innerClassId);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Assert.assertEquals("class name used as type id", resolver.getDescForKnownTypeIds());
    }

    private enum TestEnum {
        VALUE
    }

    private enum TestSubEnum {
        VALUE
    }

    private class InnerClass {

    }

}