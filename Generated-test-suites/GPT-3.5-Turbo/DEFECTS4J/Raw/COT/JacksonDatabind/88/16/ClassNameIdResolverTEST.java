import org.junit.Test;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {

    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        resolver.registerSubtype(String.class, "java.lang.String");
    }
    
    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValue("test");
        assertEquals("java.lang.String", id);
    }
    
    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = resolver.idFromValueAndType("test", String.class);
        assertEquals("java.lang.String", id);
    }
    
    @Test
    public void testTypeFromId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        try {
            JavaType type = resolver.typeFromId(null, "java.lang.String");
            assertNotNull(type);
            assertEquals(String.class, type.getRawClass());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void test_typeFromId_withInvalidId_shouldThrowIllegalArgumentException() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        try {
            JavaType type = resolver.typeFromId(null, "invalid.id");
            fail("Expected IllegalArgumentException was not thrown");
        } catch (IOException e) {
            assertEquals("Invalid type id 'invalid.id' (for id type 'Id.class'): null", e.getMessage());
        }
    }

    @Test
    public void testIdFrom_withEnumSetValue_shouldReturnCorrectId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        EnumSet<SampleEnum> enumSet = EnumSet.noneOf(SampleEnum.class);
        String id = resolver.idFromValue(enumSet);
        assertEquals("java.util.EnumSet<" + SampleEnum.class.getCanonicalName() + ">", id);
    }
    
    @Test
    public void testIdFrom_withEnumMapValue_shouldReturnCorrectId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        EnumMap<SampleEnum, String> enumMap = new EnumMap<>(SampleEnum.class);
        String id = resolver.idFromValue(enumMap);
        assertEquals("java.util.EnumMap<" + SampleEnum.class.getCanonicalName() + ", " + String.class.getCanonicalName() + ">", id);
    }
    
    @Test
    public void testIdFrom_withArrayListValue_shouldReturnCorrectId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        ArrayList<String> list = new ArrayList<>();
        String id = resolver.idFromValue(list);
        assertEquals("java.util.ArrayList", id);
    }

}