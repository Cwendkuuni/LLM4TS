import org.junit.*;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ClassNameIdResolverTest {
  
    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }
  
    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        // The method is empty, there's nothing to test
    }
    
    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        Object value = new Object();
        String expected = value.getClass().getName();
        String actual = resolver.idFromValue(value);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        Object value = new Object();
        Class<?> type = value.getClass();
        String expected = type.getName();
        String actual = resolver.idFromValueAndType(value, type);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testTypeFromId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "java.lang.Object";
        DatabindContext context = new DeserializationContext(null, null);
        JavaType expected = TypeFactory.defaultInstance().constructType(Object.class);
        JavaType actual = resolver.typeFromId(context, id);
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_typeFromId_withUnknownId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "com.example.UnknownType";
        DatabindContext context = new DeserializationContext(null, null);
        JavaType expected = null;
        JavaType actual = resolver.typeFromId(context, id);
        assertEquals(expected, actual);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void test_typeFromId_withInvalidId() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "InvalidTypeName";
        DatabindContext context = new DeserializationContext(null, null);
        resolver.typeFromId(context, id);
    }
    
    @Test
    public void test_typeFromId_withEnum() throws IOException {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        String id = "java.lang.Enum";
        DatabindContext context = new DeserializationContext(null, null);
        JavaType expected = TypeFactory.defaultInstance().constructType(Enum.class);
        JavaType actual = resolver.typeFromId(context, id);
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_idFrom_withEnumSet() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        EnumSet<TestClass> value = EnumSet.allOf(TestClass.class);
        String expected = "java.util.EnumSet<TestClass>";
        String actual = resolver._idFrom(value, value.getClass(), TypeFactory.defaultInstance());
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_idFrom_withEnumMap() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        EnumMap<TestClass, Integer> value = new EnumMap<>(TestClass.class);
        String expected = "java.util.EnumMap<TestClass, java.lang.Integer>";
        String actual = resolver._idFrom(value, value.getClass(), TypeFactory.defaultInstance());
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_idFrom_withArrayList() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        ArrayList<String> value = new ArrayList<>();
        String expected = "java.util.ArrayList";
        String actual = resolver._idFrom(value, value.getClass(), TypeFactory.defaultInstance());
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_idFrom_withInnerClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, TypeFactory.defaultInstance());
        InnerTestClass.Inner value = new InnerTestClass.Inner();
        String expected = "com.fasterxml.jackson.databind.jsontype.impl.InnerTestClass.Inner";
        String actual = resolver._idFrom(value, value.getClass(), TypeFactory.defaultInstance());
        assertEquals(expected, actual);
    }
    
    @Test
    public void test_getDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expected = "class name used as type id";
        String actual = resolver.getDescForKnownTypeIds();
        assertEquals(expected, actual);
    }
    
    private enum TestClass {
        ONE, TWO, THREE
    }
    
    private static class InnerTestClass {
        private static class Inner {
            
        }
    }
}