import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ClassNameIdResolverTest {

    private JavaType _baseType;
    private TypeFactory _typeFactory;
    private ClassNameIdResolver _resolver;
    
    @Before
    public void setUp() {
        _baseType = TypeFactory.defaultInstance().constructType(Object.class);
        _typeFactory = TypeFactory.defaultInstance();
        _resolver = new ClassNameIdResolver(_baseType, _typeFactory);
    }
    
    @Test
    public void testGetMechanism() {
        JsonTypeInfo.Id mechanism = _resolver.getMechanism();
        assertEquals(JsonTypeInfo.Id.CLASS, mechanism);
    }
    
    @Test
    public void testRegisterSubtype() {
        Class<?> type = String.class;
        String name = "String";
        // Since this method is not used with class name-based resolvers, 
        // it is expected to do nothing.
        _resolver.registerSubtype(type, name);
    }
    
    @Test
    public void testIdFromValue() {
        Object value = "Test";
        String expectedId = String.class.getName();
        String id = _resolver.idFromValue(value);
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndType() {
        Object value = "Test";
        Class<?> type = String.class;
        String expectedId = String.class.getName();
        String id = _resolver.idFromValueAndType(value, type);
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testTypeFromId() throws IOException {
        String id = String.class.getName();
        JavaType expectedType = _typeFactory.constructFromCanonical(id);
        DatabindContext context = new DeserializationContext(null, null, null);
        JavaType type = _resolver.typeFromId(context, id);
        assertEquals(expectedType, type);
    }
    
    @Test
    public void testTypeFromIdUnknown() throws IOException {
        String id = "com.example.UnknownClass";
        DatabindContext context = new DeserializationContext(null, null, null);
        JavaType type = _resolver.typeFromId(context, id);
        assertNull(type);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromIdInvalid() throws IOException {
        String id = "InvalidClass";
        DatabindContext context = new DeserializationContext(null, null, null);
        _resolver.typeFromId(context, id);
    }
    
    @Test
    public void testTypeFromIdWithGenerics() throws IOException {
        String id = "java.util.Map<java.lang.String, java.lang.Integer>";
        JavaType expectedType = _typeFactory.constructFromCanonical(id);
        DatabindContext context = new DeserializationContext(null, null, null);
        JavaType type = _resolver.typeFromId(context, id);
        assertEquals(expectedType, type);
    }
    
    @Test
    public void testTypeFromIdInnerClass() throws IOException {
        String id = "com.example.OuterClass$InnerClass";
        Class<?> expectedClass = OuterClass.InnerClass.class;
        DatabindContext context = new DeserializationContext(null, null, null);
        JavaType type = _resolver.typeFromId(context, id);
        assertEquals(expectedClass, type.getRawClass());
    }
    
    @Test
    public void testTypeFromIdUnresolvedInnerClass() throws IOException {
        String id = "com.example.OuterClass$UnresolvedClass";
        DatabindContext context = new DeserializationContext(null, null, null);
        JavaType type = _resolver.typeFromId(context, id);
        assertNull(type);
    }
    
    @Test
    public void testTypeFromIdWithEnums() throws IOException {
        EnumSet<SomeEnum> enumSet = EnumSet.of(SomeEnum.VALUE1, SomeEnum.VALUE2);
        String id = _resolver.idFromValue(enumSet);
        DatabindContext context = new DeserializationContext(null, null, null);
        JavaType type = _resolver.typeFromId(context, id);
        assertEquals(EnumSet.class, type.getRawClass());
    }
    
    @Test
    public void testTypeFromIdWithEnumMap() throws IOException {
        EnumMap<SomeEnum, String> enumMap = new EnumMap<SomeEnum, String>(SomeEnum.class);
        enumMap.put(SomeEnum.VALUE1, "Test");
        String id = _resolver.idFromValue(enumMap);
        DatabindContext context = new DeserializationContext(null, null, null);
        JavaType type = _resolver.typeFromId(context, id);
        assertEquals(EnumMap.class, type.getRawClass());
    }
    
    @Test
    public void testIdFromValueAndTypeWithEnum() {
        SomeEnum value = SomeEnum.VALUE1;
        Class<?> type = SomeEnum.class;
        String expectedId = SomeEnum.class.getName();
        String id = _resolver.idFromValueAndType(value, type);
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndTypeWithSubclassOfEnum() {
        SubclassOfEnum value = SubclassOfEnum.VALUE1;
        Class<?> type = SubclassOfEnum.class;
        String expectedId = SubclassOfEnum.class.getSuperclass().getName();
        String id = _resolver.idFromValueAndType(value, type);
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndTypeWithNonEnum() {
        Object value = new Object();
        Class<?> type = Object.class;
        String expectedId = Object.class.getName();
        String id = _resolver.idFromValueAndType(value, type);
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndTypeWithInnerClass() {
        OuterClass.InnerClass value = new OuterClass.InnerClass();
        Class<?> type = OuterClass.InnerClass.class;
        String expectedId = OuterClass.InnerClass.class.getName();
        String id = _resolver.idFromValueAndType(value, type);
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndTypeWithUnresolvedInnerClass() {
        OuterClass.UnresolvedClass value = new OuterClass.UnresolvedClass();
        Class<?> type = OuterClass.UnresolvedClass.class;
        String expectedId = OuterClass.UnresolvedClass.class.getName();
        String id = _resolver.idFromValueAndType(value, type);
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndTypeWithArrayWrapper() {
        Integer[] array = new Integer[] { 1, 2, 3 };
        Class<?> type = array.getClass();
        String expectedId = ArrayList.class.getName();
        String id = _resolver.idFromValueAndType(array, type);
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndTypeWithListWrapper() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Class<?> type = list.getClass();
        String expectedId = ArrayList.class.getName();
        String id = _resolver.idFromValueAndType(list, type);
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testGetDescForKnownTypeIds() {
        String expectedDesc = "class name used as type id";
        String desc = _resolver.getDescForKnownTypeIds();
        assertEquals(expectedDesc, desc);
    }
}