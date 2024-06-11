import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.InnerClassProperty;

public class InnerClassPropertyTest {

    @Test
    public void testInnerClassPropertyConstruction() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"class\":\"com.example.InnerClass\", \"innerClass\":{\"name\":\"InnerName\"}}";
        InnerClass innerClass = mapper.readValue(json, InnerClass.class);
        InnerName innerName = innerClass.innerClass;
        assertNotNull(innerName);
        assertEquals("InnerName", innerName.name);
    }

    // ...
}

class InnerClass {
    private InnerName innerClass;

    public InnerClass() {
        // default constructor
    }

    public InnerClass(String name) {
        this.innerClass = new InnerName(name);
    }

    public InnerName getInnerClass() {
        return innerClass;
    }

    static class InnerName {
        private String name;

        public InnerName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}