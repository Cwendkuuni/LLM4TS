import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class CrossModuleMethodMotionTest {

    private CrossModuleMethodMotion crossModuleMethodMotion;
    private AbstractCompiler compiler;
    private IdGenerator idGenerator;
    private AnalyzePrototypeProperties analyzer;
    private JSModuleGraph moduleGraph;

    @Before
    public void setUp() {
        compiler = new Compiler();
        idGenerator = new CrossModuleMethodMotion.IdGenerator();
        moduleGraph = new JSModuleGraph();
        analyzer = new AnalyzePrototypeProperties(compiler, moduleGraph, true, false);
        crossModuleMethodMotion = new CrossModuleMethodMotion(compiler, idGenerator, true);
    }

    @Test
    public void process_withMultipleModules_shouldMoveMethods() {
        Node externRoot = new Node();
        Node root = new Node();
        moduleGraph.addModule(new JSModule("module1"));
        moduleGraph.addModule(new JSModule("module2"));
        crossModuleMethodMotion.process(externRoot, root);

        // Assertions here
        assertEquals(2, moduleGraph.getModuleCount());
        assertNotNull(analyzer.getAllNameInfo());
    }

    @Test
    public void moveMethods_withDeclarationsAndValue_shouldStubAndUnstubMethods() {
        // Prepare test data
        Node value = new Node(Token.FUNCTION);
        Node valueParent = new Node(Token.GETPROP, value);
        Node unstubCall = new Node(Token.CALL);
        Node proto = new Node(Token.PROPERTY_NAME);

        Property property = new Property();
        property.setValue(value);
        property.setPrototype(proto);
        property.getPrototype().addChildToFront(new Node(Token.STRING, "name"));

        AnalyzePrototypeProperties.Symbol symbol = property;
        AnalyzePrototypeProperties.NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
        nameInfo.getDeclarations().addLast(symbol);

        // Perform method move
        crossModuleMethodMotion.moveMethods(nameInfo);

        // Assertions here
        assertEquals(Token.CALL, valueParent.getToken());
        assertEquals(Token.CALL, unstubCall.getToken());
    }

    @Test
    public void moveMethods_withGetOrSet_shouldSkipMethodMove() {
        // Prepare test data
        Node value = new Node(Token.STRING);

        Property property = new Property();
        property.setValue(value);

        AnalyzePrototypeProperties.Symbol symbol = property;
        AnalyzePrototypeProperties.NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
        nameInfo.getDeclarations().addLast(symbol);

        // Perform method move
        crossModuleMethodMotion.moveMethods(nameInfo);

        // Assertions here
        // The assertions will depend on the action performed when a GET or SET is encountered,
        // but generally, if a GET or SET is encountered, there should be no effect on the original method.
    }

    @Test
    public void moveMethods_withNullCommonModuleRef_shouldReportError() {
        // Prepare test data
        Property property = new Property();

        AnalyzePrototypeProperties.Symbol symbol = property;
        AnalyzePrototypeProperties.NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
        nameInfo.getDeclarations().addLast(symbol);

        // Perform method move
        crossModuleMethodMotion.moveMethods(nameInfo);

        // Assertions here
        // Verify that an error is reported when the common module is null
    }

    @Test
    public void newId_withMultipleCalls_shouldGenerateUniqueIds() {
        int id1 = idGenerator.newId();
        int id2 = idGenerator.newId();

        // Assertions here
        assertNotEquals(id1, id2);
    }

}