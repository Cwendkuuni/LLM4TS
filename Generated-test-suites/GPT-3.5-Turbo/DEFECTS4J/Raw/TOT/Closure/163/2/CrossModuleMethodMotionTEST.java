import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CrossModuleMethodMotion;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.Symbol;
import com.google.javascript.jscomp.JSModuleGraph;
import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.JSModule;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collection;

public class CrossModuleMethodMotionTest {
  
  private CrossModuleMethodMotion classUnderTest;
  private AbstractCompiler compiler;
  private IdGenerator idGenerator;
  private JSModuleGraph moduleGraph;

  @Before
  public void setup() {

    compiler = Mockito.mock(AbstractCompiler.class);
    idGenerator = Mockito.mock(IdGenerator.class);
    moduleGraph = Mockito.mock(JSModuleGraph.class);

    classUnderTest = new CrossModuleMethodMotion(compiler, idGenerator, true);
  }

  @Test
  public void process_withSingleModule_shouldNotMoveMethods() {
    // Arrange
    Node externRoot = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);
    Symbol symbol = Mockito.mock(Symbol.class);
    Mockito.when(moduleGraph.getModuleCount()).thenReturn(1);
    AnalyzePrototypeProperties.NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
    nameInfo.addDeclaration(symbol);
    Collection<NameInfo> allNameInfo = new ArrayList<>();
    allNameInfo.add(nameInfo);
    Mockito.when(analyzer.getAllNameInfo()).thenReturn(allNameInfo);

    // Act
    classUnderTest.process(externRoot, root);

    // Assert
    Mockito.verify(analyzer, Mockito.times(0)).process(externRoot, root);
    Mockito.verify(compiler, Mockito.times(0)).report(Mockito.any(JSError.class));
    Mockito.verify(compiler, Mockito.times(0)).getNodeForCodeInsertion(Mockito.any(JSModule.class));
    Mockito.verify(compiler, Mockito.times(0)).reportCodeChange();
  }

  @Test
  public void process_withMultipleModule_shouldMoveMethods() {
    // Arrange
    Node externRoot = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);
    Symbol symbol = Mockito.mock(Symbol.class);
    Mockito.when(moduleGraph.getModuleCount()).thenReturn(2);
    AnalyzePrototypeProperties.NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
    nameInfo.addDeclaration(symbol);
    Collection<NameInfo> allNameInfo = new ArrayList<>();
    allNameInfo.add(nameInfo);
    Mockito.when(analyzer.getAllNameInfo()).thenReturn(allNameInfo);

    // Act
    classUnderTest.process(externRoot, root);

    // Assert
    Mockito.verify(analyzer, Mockito.times(1)).process(externRoot, root);
    Mockito.verify(compiler, Mockito.times(0)).report(Mockito.any(JSError.class));
    Mockito.verify(compiler, Mockito.times(1)).getNodeForCodeInsertion(Mockito.any(JSModule.class));
    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
  }

  @Test
  public void process_withNullCommonModule_shouldReportError() {
    // Arrange
    Node externRoot = Mockito.mock(Node.class);
    Node root = Mockito.mock(Node.class);
    Symbol symbol = Mockito.mock(Symbol.class);
    Mockito.when(moduleGraph.getModuleCount()).thenReturn(2);
    AnalyzePrototypeProperties.NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
    nameInfo.addDeclaration(symbol);
    nameInfo.setDeepestCommonModuleRef(null);
    Collection<NameInfo> allNameInfo = new ArrayList<>();
    allNameInfo.add(nameInfo);
    Mockito.when(analyzer.getAllNameInfo()).thenReturn(allNameInfo);

    // Act
    classUnderTest.process(externRoot, root);

    // Assert
    Mockito.verify(analyzer, Mockito.times(1)).process(externRoot, root);
    Mockito.verify(compiler, Mockito.times(1)).report(Mockito.any(JSError.class));
    Mockito.verify(compiler, Mockito.times(0)).getNodeForCodeInsertion(Mockito.any(JSModule.class));
    Mockito.verify(compiler, Mockito.times(0)).reportCodeChange();
  }

  @Test
  public void moveMethods_withUnreferencedNameInfo_shouldNotMoveMethods() {
    // Arrange
    Symbol symbol = Mockito.mock(Symbol.class);
    NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
    nameInfo.addDeclaration(symbol);

    // Act
    classUnderTest.moveMethods(Collections.singleton(nameInfo));

    // Assert
    // Nothing to assert as the method doesn't perform any action
  }

  @Test
  public void moveMethods_withReadsClosureVariables_shouldNotMoveMethods() {
    // Arrange
    Symbol symbol = Mockito.mock(Symbol.class);
    NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
    nameInfo.addDeclaration(symbol);
    nameInfo.setReferenced(true);
    nameInfo.setReadsClosureVariables(true);

    // Act
    classUnderTest.moveMethods(Collections.singleton(nameInfo));

    // Assert
    // Nothing to assert as the method doesn't perform any action
  }

  @Test
  public void moveMethods_withFunctionProperty_shouldMoveMethods() {
    // Arrange
    Symbol symbol = Mockito.mock(Symbol.class);
    NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
    nameInfo.addDeclaration(symbol);
    nameInfo.setReferenced(true);
    nameInfo.setReadsClosureVariables(false);
    JSModule deepestCommonModuleRef = Mockito.mock(JSModule.class);
    nameInfo.setDeepestCommonModuleRef(deepestCommonModuleRef);
    nameInfo.setYield(false);

    Node value = Mockito.mock(Node.class);
    NameInfo.Property property = Mockito.mock(NameInfo.Property.class);
    Map<Integer, NameInfo.Property> map = new HashMap<>();
    map.put(0, property);

    Mockito.when(symbol instanceof NameInfo.Property).thenReturn(true);
    Mockito.when(symbol instanceof NameInfo.Symbol).thenReturn(false);
    Mockito.when(compiler.getNodeForCodeInsertion(deepestCommonModuleRef)).thenReturn(Mockito.mock(Node.class));
    Mockito.when(property.getValue()).thenReturn(value);
    Mockito.when(value.getParent()).thenReturn(Mockito.mock(Node.class));
    Mockito.when(property.getPrototype()).thenReturn(Mockito.mock(Node.class));
    Mockito.when(property.getModule()).thenReturn(Mockito.mock(JSModule.class));
    Mockito.when(moduleGraph.dependsOn(Mockito.any(JSModule.class), Mockito.any(JSModule.class))).thenReturn(true);
    Mockito.when(symbol.getMap()).thenReturn(map);
    Mockito.when(idGenerator.newId()).thenReturn(0);

    // Act
    classUnderTest.moveMethods(Collections.singleton(nameInfo));

    // Assert
    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
  }

  @Test
  public void moveMethods_withoutStubDeclaration_shouldDeclareStubFunctions() {
    // Arrange
    Symbol symbol = Mockito.mock(Symbol.class);
    NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
    nameInfo.addDeclaration(symbol);
    nameInfo.setReferenced(true);
    nameInfo.setReadsClosureVariables(false);
    JSModule deepestCommonModuleRef = Mockito.mock(JSModule.class);
    nameInfo.setDeepestCommonModuleRef(deepestCommonModuleRef);
    nameInfo.setYield(false);

    Node value = Mockito.mock(Node.class);
    NameInfo.Property property = Mockito.mock(NameInfo.Property.class);
    Map<Integer, NameInfo.Property> map = new HashMap<>();
    map.put(0, property);

    Mockito.when(symbol instanceof NameInfo.Property).thenReturn(true);
    Mockito.when(symbol instanceof NameInfo.Symbol).thenReturn(false);
    Mockito.when(compiler.getNodeForCodeInsertion(deepestCommonModuleRef)).thenReturn(Mockito.mock(Node.class));
    Mockito.when(property.getValue()).thenReturn(value);
    Mockito.when(value.getParent()).thenReturn(Mockito.mock(Node.class));
    Mockito.when(property.getPrototype()).thenReturn(Mockito.mock(Node.class));
    Mockito.when(property.getModule()).thenReturn(Mockito.mock(JSModule.class));
    Mockito.when(moduleGraph.dependsOn(Mockito.any(JSModule.class), Mockito.any(JSModule.class))).thenReturn(true);
    Mockito.when(symbol.getMap()).thenReturn(map);
    Mockito.when(idGenerator.newId()).thenReturn(0);
    Mockito.when(idGenerator.hasGeneratedAnyIds()).thenReturn(false);

    // Act
    classUnderTest.moveMethods(Collections.singleton(nameInfo));

    // Assert
    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
    Mockito.verify(compiler, Mockito.times(1)).parseSyntheticCode(Mockito.anyString());
    Mockito.verify(compiler, Mockito.times(1)).getNodeForCodeInsertion(null);
    Mockito.verify(compiler.getNodeForCodeInsertion(null), Mockito.times(1)).addChildrenToFront(Mockito.any(Node.class));
  }

  @Test
  public void moveMethods_withStubDeclaration_shouldNotDeclareStubFunctions() {
    // Arrange
    Symbol symbol = Mockito.mock(Symbol.class);
    NameInfo nameInfo = new AnalyzePrototypeProperties.NameInfo();
    nameInfo.addDeclaration(symbol);
    nameInfo.setReferenced(true);
    nameInfo.setReadsClosureVariables(false);
    JSModule deepestCommonModuleRef = Mockito.mock(JSModule.class);
    nameInfo.setDeepestCommonModuleRef(deepestCommonModuleRef);
    nameInfo.setYield(false);

    Node value = Mockito.mock(Node.class);
    NameInfo.Property property = Mockito.mock(NameInfo.Property.class);
    Map<Integer, NameInfo.Property> map = new HashMap<>();
    map.put(0, property);

    Mockito.when(symbol instanceof NameInfo.Property).thenReturn(true);
    Mockito.when(symbol instanceof NameInfo.Symbol).thenReturn(false);
    Mockito.when(compiler.getNodeForCodeInsertion(deepestCommonModuleRef)).thenReturn(Mockito.mock(Node.class));
    Mockito.when(property.getValue()).thenReturn(value);
    Mockito.when(value.getParent()).thenReturn(Mockito.mock(Node.class));
    Mockito.when(property.getPrototype()).thenReturn(Mockito.mock(Node.class));
    Mockito.when(property.getModule()).thenReturn(Mockito.mock(JSModule.class));
    Mockito.when(moduleGraph.dependsOn(Mockito.any(JSModule.class), Mockito.any(JSModule.class))).thenReturn(true);
    Mockito.when(symbol.getMap()).thenReturn(map);
    Mockito.when(idGenerator.newId()).thenReturn(0);
    Mockito.when(idGenerator.hasGeneratedAnyIds()).thenReturn(true);

    // Act
    classUnderTest.moveMethods(Collections.singleton(nameInfo));

    // Assert
    Mockito.verify(compiler, Mockito.times(1)).reportCodeChange();
    Mockito.verify(compiler, Mockito.times(0)).parseSyntheticCode(Mockito.anyString());
    Mockito.verify(compiler, Mockito.times(0)).getNodeForCodeInsertion(null);
    Mockito.verify(compiler.getNodeForCodeInsertion(null), Mockito.times(0)).addChildrenToFront(Mockito.any(Node.class));
  }
}