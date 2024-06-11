import static org.junit.Assert.*;
import org.junit.*;

import com.fasterxml.jackson.databind.ser.std.NumberSerializers;

public class NumberSerializersTest {

    private NumberSerializers numberSerializers;

    @Before
    public void setUp() {
        numberSerializers = new NumberSerializers();
    }

    @After
    public void tearDown() {
        numberSerializers = null;
    }

    @Test
    public void testAddAll() {
        // TODO: implement test
    }

    @Test
    public void testBase() {
        // TODO: implement test
    }

    @Test
    public void testShortSerializer() {
        // TODO: implement test
    }

    @Test
    public void testIntegerSerializer() {
        // TODO: implement test
    }

    @Test
    public void testIntLikeSerializer() {
        // TODO: implement test
    }

    @Test
    public void testLongSerializer() {
        // TODO: implement test
    }

    @Test
    public void testFloatSerializer() {
        // TODO: implement test
    }

    @Test
    public void testDoubleSerializer() {
        // TODO: implement test
    }
}