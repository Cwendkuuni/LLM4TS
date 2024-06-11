import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        String name = "testFunction";
        FunctionType function = builder.withName(name).build();
        assertEquals(name, function.getName());
    }

    @Test
    public void testWithSourceNode() {
        FunctionBuilder builder = new FunctionBuilder(new JSTypeRegistry());
        Node sourceNode = new Node();
        FunctionType function = builder.withSourceNode(sourceNode).build();
        assertEquals(sourceNode, function.getSourceNode());
    }

    // Add more test methods for each public method in FunctionBuilder class

}