
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonTreeReaderTest {

    private JsonTreeReader jsonTreeReader;

    @Before
    public void setUp() {
        jsonTreeReader = new JsonTreeReader(new JsonObject());
    }

    @Test
    public void testBeginArray() throws Exception {
        jsonTreeReader.beginArray();
    }

    @Test
    public void testEndArray() throws Exception {
        jsonTreeReader.endArray();
    }

    @Test
    public void testBeginObject() throws Exception {
        jsonTreeReader.beginObject();
    }

    @Test
    public void testEndObject() throws Exception {
        jsonTreeReader.endObject();
    }

    @Test
    public void testHasNext() throws Exception {
        jsonTreeReader.hasNext();
    }

    @Test
    public void testPeek() throws Exception {
        jsonTreeReader.peek();
    }

    @Test
    public void testNextName() throws Exception {
        jsonTreeReader.nextName();
    }

    @Test
    public void testNextString() throws Exception {
        jsonTreeReader.nextString();
    }

    @Test
    public void testNextBoolean() throws Exception {
        jsonTreeReader.nextBoolean();
    }

    @Test
    public void testNextNull() throws Exception {
        jsonTreeReader.nextNull();
    }

    @Test
    public void testNextDouble() throws Exception {
        jsonTreeReader.nextDouble();
    }

    @Test
    public void testNextLong() throws Exception {
        jsonTreeReader.nextLong();
    }

    @Test
    public void testNextInt() throws Exception {
        jsonTreeReader.nextInt();
    }

    @Test
    public void testClose() throws Exception {
        jsonTreeReader.close();
    }

    @Test
    public void testSkipValue() throws Exception {
        jsonTreeReader.skipValue();
    }

    @Test
    public void testPromoteNameToValue() throws Exception {
        jsonTreeReader.promoteNameToValue();
    }

    @Test
    public void testGetPath() throws Exception {
        assertEquals("$", jsonTreeReader.getPath());
    }
}
