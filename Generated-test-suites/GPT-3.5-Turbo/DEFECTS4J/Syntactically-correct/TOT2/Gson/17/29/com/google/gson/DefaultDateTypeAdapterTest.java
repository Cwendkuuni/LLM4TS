import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.text.ParseException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.Assert.assertEquals;

public class DefaultDateTypeAdapterTest {

    private DefaultDateTypeAdapter adapter;

    @Before
    public void setup() {
        adapter = new DefaultDateTypeAdapter(Date.class);
    }

    @Test
    public void testWrite() throws IOException {
        Date currentDate = new Date();
        // Test writing the current date
        assertEquals(adapter.write(currentDate), currentDate);
    }

    @Test
    public void testRead() throws IOException {
        String dateString = "2021-10-15";
        Date expectedDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        // Test reading a date string
        assertEquals(adapter.read(dateString), expectedDate);
    }
}