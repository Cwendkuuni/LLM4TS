
import org.junit.Test;
import com.google.gson.Gson;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class ReflectiveTypeAdapterFactoryTest {
    
    private ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory;
    private Gson gson;
    private TypeAdapterFactory typeAdapterFactory;
    
    @Before
    public void setup() {
        ConstructorConstructor constructorConstructor = new ConstructorConstructor();
        FieldNamingStrategy fieldNamingStrategy = FieldNamingPolicy.IDENTITY;
        Excluder excluder = Excluder.DEFAULT;
        reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory(constructorConstructor, fieldNamingStrategy, excluder);
        gson = new Gson();
    }
    
    @Test
    public void testExcludeField_validField_returnFalse() {
        Field field = getClass().getDeclaredField("reflectiveTypeAdapterFactory");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertFalse(result);
    }
    
    @Test
    public void testExcludeField_invalidField_returnTrue() {
        Field field = getClass().getDeclaredField("gson");
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, true);
        assertTrue(result);
    }
    
    @Test
    public void testGetFieldNames_noSerializedNameAnnotation_returnListWithDefaultName() {
        Field field = getClass().getDeclaredField("reflectiveTypeAdapterFactory");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        List<String> expectedFieldNames = Collections.singletonList("reflectiveTypeAdapterFactory");
        assertEquals(expectedFieldNames, fieldNames);
    }
    
    @Test
    public void testGetFieldNames_withSerializedNameAnnotation_returnListWithSerializedName() {
        Field field = getClass().getDeclaredField("gson");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        List<String> expectedFieldNames = Collections.singletonList("gson");
        assertEquals(expectedFieldNames, fieldNames);
    }
    
    @Test
    public void testGetFieldNames_withAlternateSerializedNameAnnotation_returnListWithSerializedNameAndAlternates() {
        Field field = getClass().getDeclaredField("typeAdapterFactory");
        List<String> fieldNames = reflectiveTypeAdapterFactory.getFieldNames(field);
        List<String> expectedFieldNames = new ArrayList<String>() {{
            add("typeAdapter");
            add("alternateTypeAdapter");
            add("alternateTypeAdapter2");
        }};
        assertEquals(expectedFieldNames, fieldNames);
    }
    
    @Test
    public void testCreate_withPrimitiveType_returnNull() {
        TypeToken<Integer> typeToken = new TypeToken<Integer>() {};
        TypeAdapter<Integer> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNull(result);
    }
    
    @Test
    public void testCreate_withObjectType_returnAdapterObject() {
        TypeToken<ReflectiveTypeAdapterFactory> typeToken = new TypeToken<ReflectiveTypeAdapterFactory>() {};
        TypeAdapter<ReflectiveTypeAdapterFactory> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertNotNull(result);
    }
    
    @Test
    public void testCreate_withInterfaceType_returnEmptyMap() {
        TypeToken<Map> typeToken = new TypeToken<Map>() {};
        TypeAdapter<Map> result = reflectiveTypeAdapterFactory.create(gson, typeToken);
        assertTrue(result instanceof ReflectiveTypeAdapterFactory.Adapter);
        ReflectiveTypeAdapterFactory.Adapter<Map> adapter = (ReflectiveTypeAdapterFactory.Adapter<Map>) result;
        assertTrue(adapter.boundFields.isEmpty());
    }
    
    @Test
    public void testCreateBoundField_withJsonAdapterAnnotation_returnBoundFieldWithMappedTypeAdapter() throws NoSuchFieldException {
        Field field = getClass().getDeclaredField("gson");
        TypeToken<?> fieldType = TypeToken.get(Gson.class);
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, field, "gson", fieldType, true, true);
        assertTrue(boundField instanceof ReflectiveTypeAdapterFactory.BoundField);
        ReflectiveTypeAdapterFactory.BoundField bf = (ReflectiveTypeAdapterFactory.BoundField) boundField;
        assertNotNull(bf);
        assertSame(bf.typeAdapter, gson.getAdapter(Gson.class));
    }
    
    @Test
    public void testCreateBoundField_withoutJsonAdapterAnnotation_returnBoundFieldWithDefaultTypeAdapter() throws NoSuchFieldException {
        Field field = getClass().getDeclaredField("reflectiveTypeAdapterFactory");
        TypeToken<?> fieldType = TypeToken.get(ReflectiveTypeAdapterFactory.class);
        ReflectiveTypeAdapterFactory.BoundField boundField = reflectiveTypeAdapterFactory.createBoundField(gson, field, "reflectiveTypeAdapterFactory", fieldType, true, true);
        assertTrue(boundField instanceof ReflectiveTypeAdapterFactory.BoundField);
        ReflectiveTypeAdapterFactory.BoundField bf = (ReflectiveTypeAdapterFactory.BoundField) boundField;
        assertNotNull(bf);
        assertNotSame(bf.typeAdapter, gson.getAdapter(ReflectiveTypeAdapterFactory.class));
    }
    
    @Test
    public void testGetBoundFields_withObjectType_returnNonEmptyMap() {
        TypeToken<ReflectiveTypeAdapterFactory> typeToken = new TypeToken<ReflectiveTypeAdapterFactory>() {};
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, ReflectiveTypeAdapterFactory.class);
        assertFalse(boundFields.isEmpty());
    }
    
    @Test
    public void testGetBoundFields_withInterfaceType_returnEmptyMap() {
        TypeToken<Map> typeToken = new TypeToken<Map>() {};
        Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = reflectiveTypeAdapterFactory.getBoundFields(gson, typeToken, Map.class);
        assertTrue(boundFields.isEmpty());
    }
    
    @Test
    public void testAdapterRead_withNullToken_returnNull() throws IOException {
        TypeToken<ReflectiveTypeAdapterFactory> typeToken = new TypeToken<ReflectiveTypeAdapterFactory>() {};
        ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory> adapter = new ReflectiveTypeAdapterFactory.Adapter(new ObjectConstructor<ReflectiveTypeAdapterFactory>() {
            public ReflectiveTypeAdapterFactory construct() {
                return new ReflectiveTypeAdapterFactory(null, null, null);
            }
        }, null);
        Gson gson = new Gson();
        JsonReader in = new JsonReader();
        ReflectionTypeAdapterFactory result = adapter.read(in);
        assertNull(result);
    }
    
    @Test
    public void testAdapterWrite_withNullValue_returnNullValue() throws IOException {
        TypeToken<ReflectiveTypeAdapterFactory> typeToken = new TypeToken<ReflectiveTypeAdapterFactory>() {};
        ReflectiveTypeAdapterFactory.Adapter<ReflectiveTypeAdapterFactory> adapter = new ReflectiveTypeAdapterFactory.Adapter(new ObjectConstructor<ReflectiveTypeAdapterFactory>() {
            public ReflectiveTypeAdapterFactory construct() {
                return new ReflectiveTypeAdapterFactory(null, null);
            }
        }, null);
        Gson gson = new Gson();
        JsonWriter out = new JsonWriter();
        adapter.write(out, null);
        String output = out.toString();
        assertEquals("null", output);
    }
}
