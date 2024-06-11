import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FunctionToBlockMutatorTest {

    private FunctionToBlockMutator functionToBlockMutator;

    @Before
    public void setUp() {
        functionToBlockMutator = new FunctionToBlockMutator(new AbstractCompiler(), new Supplier<String>() {
            @Override
            public String get() {
                return "";
            }
        });
    }

    @Test
    public void testMutate_noArgs() {
        String fnName = "testFunction";
        Node fnNode = new Node(
                new Node(Token.STRING, "Test Function"),
                new Node(Token.ES6_FN))
                .copyInformationFrom(new Node(Token.ES6_FN));

        Node callNode = new Node(Token.CALL);

        String resultName = "result";
        boolean needsDefaultResult = true;
        boolean isCallInLoop = false;

        Node result = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

        // Assert statement here
    }

    @Test
    public void testMutate_withArgs() {
        String fnName = "testFunction";
        Node fnNode = new Node(
                new Node(Token.STRING, "Test Function"),
                new Node(Token.ES6_FN))
                .copyInformationFrom(new Node(Token.ES6_FN));

        Node callNode = new Node(Token.CALL);

        String resultName = "result";
        boolean needsDefaultResult = true;
        boolean isCallInLoop = false;

        Node result = functionToBlockMutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

        // Assert statement here
    }

}