import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ClassNameIdResolverTest {

    ClassNameIdResolver classNameIdResolver;

    @Mock
    TypeFactory typeFactory;

    @Mock
    DeserializationContext deserializationContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        classNameIdResolver = new ClassNameIdResolver(JavaType.class, typeFactory);
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }

    @Test
    public void testIdFromValue() {
        Object value = new Object();
        String expectedId = "java.lang.Object";
        assertEquals(expectedId, classNameIdResolver.idFromValue(value));
    }

    @Test
    public void testIdFromValueAndType() {
        Object value = new Object();
        Class<?> type = Object.class;
        String expectedId = "java.lang.Object";
        assertEquals(expectedId, classNameIdResolver.idFromValueAndType(value, type));
    }

    @Test
    public void testTypeFromId() throws IOException {
        String id = "java.lang.Object";
        JavaType expectedType = typeFactory.constructType(Object.class);
        assertEquals(expectedType, classNameIdResolver.typeFromId(deserializationContext, id));
    }

    @Test
    public void test_typeFromId_withInvalidId_shouldThrowException() {
        String id = "non.existent.package.InvalidClass";
        assertThrows(IOException.class, () -> classNameIdResolver.typeFromId(deserializationContext, id));
    }

    @Test
    public void test_idFrom_withEnumSubtype() {
        EnumSet<TestEnum> value = EnumSet.allOf(TestEnum.class);
        String expectedId = typeFactory.constructCollectionType(EnumSet.class, TestEnum.class).toCanonical();
        assertEquals(expectedId, classNameIdResolver._idFrom(value, EnumSet.class, typeFactory));
    }

    @Test
    public void test_getDescForKnownTypeIds() {
        assertEquals("class name used as type id", classNameIdResolver.getDescForKnownTypeIds());
    }

    enum TestEnum {
        ENUM1, ENUM2, ENUM3
    }
}