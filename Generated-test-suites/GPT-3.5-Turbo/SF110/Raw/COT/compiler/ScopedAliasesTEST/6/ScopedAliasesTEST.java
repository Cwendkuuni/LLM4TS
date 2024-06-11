
import static org.junit.Assert.*;
import org.junit.Test;

public class ScopedAliasesTest {

  @Test
  public void testAliasedNode() {
    Node aliasReference = new Node();
    Node aliasDefinition = new Node();
    AliasedNode aliasedNode = new AliasedNode(aliasReference, aliasDefinition);

    aliasedNode.applyAlias();

    assertEquals(aliasDefinition, aliasReference.getParent().getChild(0));
  }

  @Test
  public void testAliasedTypeNode() {
    Node typeReference = new Node();
    Node aliasDefinition = new Node();
    String aliasName = "alias";
    String typeName = "alias.Type";

    typeReference.setString(typeName);
    AliasTypeNode aliasedTypeNode = new AliasTypeNode(typeReference, aliasDefinition, aliasName);

    aliasedTypeNode.applyAlias();

    assertEquals("alias.Type", typeReference.getString());
  }

  @Test
  public void testScopedAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler();
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node externs = new Node();
    Node root = new Node();
    scopedAliases.process(externs, root);

    // Perform assertions here
  }
}
