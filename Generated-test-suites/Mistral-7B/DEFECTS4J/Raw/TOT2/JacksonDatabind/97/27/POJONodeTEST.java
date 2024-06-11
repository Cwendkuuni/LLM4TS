import static org.junit.Assert.assertEquals;
import com.fasterxml.jackson.databind.JsonNodeType;

public class POJONodeTest {
    @org.junit.Test
    public void testgetNodeType() {
        POJONode node = new POJONode(new Object());
        assertEquals(JsonNodeType.POJO, node.getNodeType());
    }
}