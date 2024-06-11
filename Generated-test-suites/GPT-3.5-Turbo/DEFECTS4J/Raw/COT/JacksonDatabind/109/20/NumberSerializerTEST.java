
import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void serialize_NullValue_ShouldSerializeNull() {
        NumberSerializer serializer = new NumberSerializer(null);
        // ...
    }

    @Test
    public void serialize_BigDecimalValue_ShouldSerializeBigDecimal() {
        NumberSerializer serializer = new NumberSerializer(BigDecimal.class);
        // ...
    }

    // ...

    @Test
    public void serialize_NonNumberValue_ShouldSerializeAsString() {
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // ...
    }

    // ...

}
