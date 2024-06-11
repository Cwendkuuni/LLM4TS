import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.node.POJONode;

public class POJONodeTest {
    @Test
    public void testgetNodeType() {
        POJONode pojoNode = new POJONode(new Object());
        JsonNodeType expectedNodeType = JsonNodeType.POJO;
        JsonNodeType actualNodeType = pojoNode.getNodeType();
        assertEquals(expectedNodeType, actualNodeType);
    }
}