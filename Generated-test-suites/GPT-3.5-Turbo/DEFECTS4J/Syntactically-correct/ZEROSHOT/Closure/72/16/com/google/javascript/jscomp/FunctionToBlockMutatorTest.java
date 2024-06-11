import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.google.javascript.rhino.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class FunctionToBlockMutatorTest {
    private AbstractCompiler compiler;
    private Supplier<String> safeNameIdSupplier;
    private FunctionToBlockMutator functionToBlockMutator;

    @Before
    public void setUp() throws Exception {
        compiler = new AbstractCompiler();
        safeNameIdSupplier = Suppliers.ofInstance("testId");
        functionToBlockMutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    }

    @Test
    public void testMutate() {
        // Test case 1
        String fnName = "fn1";
        Node fnNode = new Node(Token.FUNCTION);
        Node callNode = new Node(Token.CALL);
        String resultName = "result1";
        boolean needsDefaultResult = true;
        boolean isCallInLoop = false;

        Node result = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
        Assert.assertEquals(new Node(Token.BLOCK), result);

        // Test case 2
        fnName = "";
        fnNode = new Node(Token.FUNCTION);
        callNode = new Node(Token.CALL);
        resultName = "result2";
        needsDefaultResult = false;
        isCallInLoop = true;

        result = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);
        Assert.assertEquals(new Node(Token.BLOCK), result);
    }
}