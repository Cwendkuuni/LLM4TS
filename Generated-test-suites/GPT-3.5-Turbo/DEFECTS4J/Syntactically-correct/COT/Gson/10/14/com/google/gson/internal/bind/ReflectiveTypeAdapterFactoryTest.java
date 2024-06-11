
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Field;
import java.util.Map;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testExcludeField() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, new Excluder());
        try {
            Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
            field.setAccessible(true);
            ConstructorConstructor constructorConstructor = (ConstructorConstructor) field.get(factory);
            assertTrue(factory.excludeField(field, true));
            assertTrue(factory.excludeField(field, false));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetFieldNames() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, new Excluder());
        try {
            Field field = ReflectiveTypeAdapterFactory.class.getDeclaredField("constructorConstructor");
            field.setAccessible(true);
            ConstructorConstructor constructorConstructor = (ConstructorConstructor) field.get(factory);
            field = ReflectiveTypeAdapterFactory.class.getDeclaredField("fieldNamingPolicy");
            field.setAccessible(true);
            FieldNamingStrategy fieldNamingStrategy = (FieldNamingStrategy) field.get(factory);
            field = ReflectiveTypeAdapterFactory.class.getDeclaredField("excluder");
            field.setAccessible(true);
            Excluder excluder = (Excluder) field.get(factory);

            field = ReflectiveTypeAdapterFactory.class.getDeclaredField("excluder");
            field.setAccessible(true);
            Gson gson = new Gson();

            SerializedName annotation = ReflectiveTypeAdapterFactory.class.getDeclaredField("boundFields").getAnnotation(SerializedName.class);

            field = ReflectiveTypeAdapterFactory.class.getDeclaredField("bindFields");
            field.setAccessible(true);
            Map<String, ReflectiveTypeAdapterFactory.BoundField> boundFields = (Map<String, ReflectiveTypeAdapterFactory.BoundField>) field.get(factory);
            List<String> expectedFieldNames = new ArrayList<>(Collections.singletonList("name"));

            ReflectiveTypeAdapterFactory.BoundField boundField = factory.createBoundField(gson, (Field) field, "name", TypeToken.getType(), true, true);
            assertEquals(expectedFieldNames, factory.getFieldNames(field));
        } catch (IllegalAccessException | NoSuchFieldException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testCreate() {
        ReflectiveTypeAdapterFactory factory = new ReflectiveTypeAdapterFactory(new ConstructorConstructor(), null, new Excluder());
        Gson gson = new Gson();
        TypeToken typeToken = TypeToken.get(ReflectiveTypeAdapterFactory.Adapter.class);
        TypeAdapter adapter = factory.create(gson, typeToken);
        assertNotNull(adapter);
    }

}

