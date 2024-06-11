import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class CrossModuleMethodMotionTest {

    private CrossModuleMethodMotion crossModuleMethodMotion;

    @Before
    public void setUp() {
        crossModuleMethodMotion = new CrossModuleMethodMotion(new AbstractCompiler(), new CrossModuleMethodMotion.IdGenerator(), true);
    }

    @Test
    public void testProcess() {
        crossModuleMethodMotion.process(null, null);
    }

    @Test
    public void testMoveMethods() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        CrossModuleMethodMotion.IdGenerator.IdStubbing idStubbing = idGenerator.newId();
        CrossModuleMethodMotion.IdGenerator.IdStubbing deepModuleStubbing = idGenerator.newId();

        // Create name info
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setType("method");
        symbol.setName("testMethod");
        symbol.setModule("module1");
        symbol.setValue(new Node("return \"testMethod\";"));
        nameInfo.addDeclaration(symbol);

        // Create deepest common module ref
        CrossModuleMethodMotion.JSModule deepestCommonModuleRef = new CrossModuleMethodMotion.JSModule();

        // Set deepest common module ref
        nameInfo.setDeepestCommonModuleRef(deepestCommonModuleRef);

        // Create analyzer
        CrossModuleMethodMotion.AnalyzePrototypeProperties analyzePrototypeProperties = new CrossModuleMethodMotion.AnalyzePrototypeProperties();
        analyzePrototypeProperties.setModuleGraph(null);
        analyzePrototypeProperties.setCanModifyExterns(true);

        // Set analyzer
        crossModuleMethodMotion.analyzer = analyzePrototypeProperties;

        // Move methods
        crossModuleMethodMotion.moveMethods(nameInfo);

        // Check if method has been moved and replaced with stub
        assertEquals("JSCompiler_stubMethod(id);", symbol.getValue().toString());

        // Set deepest common module ref to null
        nameInfo.setDeepestCommonModuleRef(null);

        // Move methods when deepest common module ref is null
        crossModuleMethodMotion.moveMethods(nameInfo);

        // Verify that JSError is reported with null common module
        assertTrue(hasReportedError(CrossModuleMethodMotion.NULL_COMMON_MODULE_ERROR));
    }

    @Test
    public void testIdGenerator() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();

        // Verify initial values
        assertEquals(0, idGenerator.currentId);

        // Generate new ids
        int id = idGenerator.newId();
        int id2 = idGenerator.newId();

        // Verify generated ids
        assertEquals(1, id);
        assertEquals(2, id2);

        // Verify hasGeneratedAnyIds method
        assertTrue(idGenerator.hasGeneratedAnyIds());
    }

    @Test
    public void testProcessWithModuleGraph() {
        CrossModuleMethodMotion crossModuleMethodMotion = new CrossModuleMethodMotion(new AbstractCompiler(), new CrossModuleMethodMotion.IdGenerator(), true);
        crossModuleMethodMotion.moduleGraph = new CrossModuleMethodMotion.JSModuleGraph();

        crossModuleMethodMotion.process(null, null);
    }

    @Test
    public void testMoveMethodsWhenValueIsNotFunction() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        CrossModuleMethodMotion.IdGenerator.IdStubbing idStubbing = idGenerator.newId();

        // Create name info
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setType("property");
        symbol.setName("testProperty");
        symbol.setModule("module1");
        symbol.setValue(new Node("\"testProperty\""));
        nameInfo.addDeclaration(symbol);

        // Create deepest common module ref
        CrossModuleMethodMotion.JSModule deepestCommonModuleRef = new CrossModuleMethodMotion.JSModule();

        // Set deepest common module ref
        nameInfo.setDeepestCommonModuleRef(deepestCommonModuleRef);

        // Create analyzer
        CrossModuleMethodMotion.AnalyzePrototypeProperties analyzePrototypeProperties = new CrossModuleMethodMotion.AnalyzePrototypeProperties();
        analyzePrototypeProperties.setModuleGraph(null);
        analyzePrototypeProperties.setCanModifyExterns(true);

        // Set analyzer
        crossModuleMethodMotion.analyzer = analyzePrototypeProperties;

        // Move methods when value is not a function
        crossModuleMethodMotion.moveMethods(nameInfo);

        // Verify that no changes are made
        assertEquals("\"testProperty\"", symbol.getValue().toString());
    }

    @Test
    public void testMoveMethodsWhenPropertyIsGetterOrSetter() {
        CrossModuleMethodMotion.IdGenerator idGenerator = new CrossModuleMethodMotion.IdGenerator();
        CrossModuleMethodMotion.IdGenerator.IdStubbing idStubbing = idGenerator.newId();

        // Create name info
        CrossModuleMethodMotion.NameInfo nameInfo = new CrossModuleMethodMotion.NameInfo();
        CrossModuleMethodMotion.Symbol symbol = new CrossModuleMethodMotion.Symbol();
        symbol.setType("method");
        symbol.setName("testMethod");
        symbol.setModule("module1");
        symbol.setValue(new Node("get function() { return \"testMethod\"; }"));
        nameInfo.addDeclaration(symbol);

        // Create deepest common module ref
        CrossModuleMethodMotion.JSModule deepestCommonModuleRef = new CrossModuleMethodMotion.JSModule();

        // Set deepest common module ref
        nameInfo.setDeepestCommonModuleRef(deepestCommonModuleRef);

        // Create analyzer
        CrossModuleMethodMotion.AnalyzePrototypeProperties analyzePrototypeProperties = new CrossModuleMethodMotion.AnalyzePrototypeProperties();
        analyzePrototypeProperties.setModuleGraph(null);
        analyzePrototypeProperties.setCanModifyExterns(true);

        // Set analyzer
        crossModuleMethodMotion.analyzer = analyzePrototypeProperties;

        // Move methods when property is getter
        crossModuleMethodMotion.moveMethods(nameInfo);

        // Verify that no changes are made
        assertEquals("get function() { return \"testMethod\"; }", symbol.getValue().toString());

        // Reset type
        symbol.setType("property");

        // Set value as setter
        symbol.setValue(new Node("set function(value) { this.value = value; }"));

        // Move methods when property is setter
        crossModuleMethodMotion.moveMethods(nameInfo);

        // Verify that no changes are made
        assertEquals("set function(value) { this.value = value; }", symbol.getValue().toString());
    }

    private boolean hasReportedError(DiagnosticType errorType) {
        return false;
    }
}