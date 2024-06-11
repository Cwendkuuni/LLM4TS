import com.google.javascript.rhino.jstype.*;
import com.google.javascript.rhino.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionBuilderTest {

    @Test
    public void testWithName() {
        FunctionBuilder fb = new FunctionBuilder(new JSTypeRegistry());
        FunctionType ft = fb.withName("testWithName").build();
        assertEquals("testWithName", ft.getReferenceName());
    }

    @Test
    public void testWithSourceNode() {
        Node sourceNode = new Node(Token.EMPTY);
        FunctionBuilder fb = new FunctionBuilder(new JSTypeRegistry());
        FunctionType ft = fb.withSourceNode(sourceNode).build();
        assertEquals(sourceNode, ft.getSource());
    }

    @Test
    public void testWithParams() {
        FunctionBuilder fb = new FunctionBuilder(new JSTypeRegistry());
        Node parametersNode = new Node(Token.PARAM_LIST);
        FunctionType ft = fb.withParamsNode(parametersNode).build();
        assertEquals(parametersNode, ft.getParametersNode());
    }

    // Add more test methods for other functionalities of FunctionBuilder

}