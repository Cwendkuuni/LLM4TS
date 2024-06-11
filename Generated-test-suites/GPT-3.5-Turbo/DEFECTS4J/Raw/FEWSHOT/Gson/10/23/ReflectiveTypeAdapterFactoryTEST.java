import static org.junit.Assert.*;
import org.junit.Test;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

public class ReflectiveTypeAdapterFactoryTest {

    @Test
    public void testExcludeField() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory();
        Field field = ReflectiveTypeAdapterFactory.class.getField("constructorConstructor");
        boolean serialize = true;
        boolean result = reflectiveTypeAdapterFactory.excludeField(field, serialize);
        assertFalse(result);
    }

    @Test
    public void testStripLeadingAndTrailingQuotes() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory();
        String str = "\"foo\"";
        String result = reflectiveTypeAdapterFactory.stripLeadingAndTrailingQuotes(str);
        assertEquals("foo", result);
    }

    @Test
    public void testStripLeadingHyphens() {
        ReflectiveTypeAdapterFactory reflectiveTypeAdapterFactory = new ReflectiveTypeAdapterFactory();
        String str = "--foo";
        String result = reflectiveTypeAdapterFactory.stripLeadingHyphens(str);
        assertEquals("foo", result);
    }
}