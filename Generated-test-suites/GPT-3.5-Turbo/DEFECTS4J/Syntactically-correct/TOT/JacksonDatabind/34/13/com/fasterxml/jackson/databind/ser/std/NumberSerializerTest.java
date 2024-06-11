import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

public class NumberSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        Number value = new BigDecimal("10.5");
        String expectedJson = "10.5";

        String actualJson = mapper.writeValueAsString(value);

        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        Number value = new BigInteger("10000000000000000000000000000000000000000000000000000");
        String expectedJson = "10000000000000000000000000000000000000000000000000000";

        String actualJson = mapper.writeValueAsString(value);

        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        Number value = 10;
        String expectedJson = "10";

        String actualJson = mapper.writeValueAsString(value);

        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeLong() throws IOException {
        Number value = 10000000000000000L;
        String expectedJson = "10000000000000000";

        String actualJson = mapper.writeValueAsString(value);

        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        Number value = 10.5;
        String expectedJson = "10.5";

        String actualJson = mapper.writeValueAsString(value);

        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        Number value = 10.5f;
        String expectedJson = "10.5";

        String actualJson = mapper.writeValueAsString(value);

        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeByte() throws IOException {
        Number value = (byte) 10;
        String expectedJson = "10";

        String actualJson = mapper.writeValueAsString(value);

        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeShort() throws IOException {
        Number value = (short) 10;
        String expectedJson = "10";

        String actualJson = mapper.writeValueAsString(value);

        Assert.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void testSerializeOtherNumber() throws IOException {
        Number value = 123456789;
        String expectedJson = "123456789";

        String actualJson = mapper.writeValueAsString(value);

        Assert.assertEquals(expectedJson, actualJson);
    }
}