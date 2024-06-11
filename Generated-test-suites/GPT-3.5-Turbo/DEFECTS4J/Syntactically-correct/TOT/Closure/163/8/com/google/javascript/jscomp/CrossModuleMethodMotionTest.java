import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CrossModuleMethodMotionTest {

  private CrossModuleMethodMotion crossModuleMethodMotion;

  @Before
  public void setUp() {
    crossModuleMethodMotion = new CrossModuleMethodMotion();
  }

  @Test
  public void testProcess_ModuleCountLessThan2_NoMoveMethods() {
    // Given
    Node externRoot = new Node();
    Node root = new Node();
    crossModuleMethodMotion.moduleGraph = new JSModuleGraph();
    crossModuleMethodMotion.moduleGraph.addModule(createModule("module1"));
    crossModuleMethodMotion.compiler = new AbstractCompiler();

    // When
    crossModuleMethodMotion.process(externRoot, root);

    // Then
    assertFalse(crossModuleMethodMotion.analyzer.processed);
  }

  @Test
  public void testProcess_WithReferencedNameInfo_NoMoveMethods() {
    // Given
    Node externRoot = new Node();
    Node root = new Node();
    crossModuleMethodMotion.moduleGraph = new JSModuleGraph();
    crossModuleMethodMotion.moduleGraph.addModule(createModule("module1"));
    crossModuleMethodMotion.compiler = new AbstractCompiler();
    crossModuleMethodMotion.analyzer = new AnalyzePrototypeProperties(crossModuleMethodMotion.compiler, crossModuleMethodMotion.moduleGraph, true, false);
    crossModuleMethodMotion.analyzer.processed = true;
    crossModuleMethodMotion.analyzer.symbolTable = new AnalyzePrototypeProperties.SymbolTable();
    AnalyzePrototypeProperties.Symbol symbol = crossModuleMethodMotion.analyzer.createPropertySymbol("foobar");
    symbol.reference = true;
    crossModuleMethodMotion.analyzer.symbolTable.add(symbol);
    
    // When
    crossModuleMethodMotion.process(externRoot, root);

    // Then
    assertFalse(crossModuleMethodMotion.moveMethodsCalled);
  }

  @Test
  public void testProcess_NullDeepestCommonModuleRef_ReportError() {
    // Given
    Node externRoot = new Node();
    Node root = new Node();
    crossModuleMethodMotion.moduleGraph = new JSModuleGraph();
    crossModuleMethodMotion.moduleGraph.addModule(createModule("module1"));
    crossModuleMethodMotion.analyzer = new AnalyzePrototypeProperties(crossModuleMethodMotion.compiler, crossModuleMethodMotion.moduleGraph, true, false);
    AnalyzePrototypeProperties.NameInfo nameInfo = crossModuleMethodMotion.analyzer.new NameInfo();
    nameInfo.declarations.add(new AnalyzePrototypeProperties.Property());
    crossModuleMethodMotion.analyzer.allNameInfo.add(nameInfo);
    
    // When
    crossModuleMethodMotion.process(externRoot, root);

    // Then
    assertTrue(crossModuleMethodMotion.compiler.errorReported);
  }

  private JSModule createModule(String name) {
    // Create module with specified name
    return new JSModule("module1");
  }

}