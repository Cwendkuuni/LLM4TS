import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class ReflectiveTypeAdapterFactoryTest extends TestCase {

    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;

    @Before
    protected void setUp() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        FieldNamingStrategy fieldNamingStrategy = new FieldNamingStrategy();
        Excluder excluder = new Excluder();
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
    }

    @Test
    public void testExcludeField() {
        ReflectiveTypeAdapterFactory excludeFieldTest = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
        boolean result = excludeFieldTest.excludeField(field, true);
        assertFalse(result);
    }

    @Test
    public void testCreate() {
        Gson gson = new Gson();
        TypeToken typeToken = new TypeToken();
        TypeAdapter typeAdapter = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNotNull(typeAdapter);
    }

    @Test
    public void testCreateBoundField() {
        Gson gson = new Gson();
        Field field = new Field();
        String name = "test";
        TypeToken<?> fieldType = new TypeToken();
        boolean serialize = true;
        boolean deserialize = false;
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, field, name, fieldType, serialize, deserialize);
        assertNotNull(boundField);
    }

    @Test
    public void testGetBoundFields() {
        Gson gson = new Gson();
        TypeToken<?> typeToken = new TypeToken();
        Class<?> raw = Object.class;
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, raw);
        assertNotNull(boundFields);
    }

    @Test
    public void testAdapter() {
        ObjectConstructor constructor = new ObjectConstructor();
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = new LinkedHashMap<>();
        ReflectiveTypeAdapterFactory.Adapter adapter = new ReflectiveTypeAdapterFactory.Adapter(constructor, boundFields);
        assertNotNull(adapter);
    }

}