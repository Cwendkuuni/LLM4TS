import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;

public class FunctionBuilderTest {

  @org.junit.Test
  public void testFunctionBuilderWithBasicFunction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "add";
    Node sourceNode = new Node(1, new Node[] {
        new Node(1, new Node[] {
          new Node(Node.IDENTIFIER, "function"),
          new Node(Node.IDENTIFIER, functionName),
          new Node(Node.LEFT_PAREN, null),
          new Node(Node.IDENTIFIER, "a"),
          new Node(Node.COMMA),
          new Node(Node.IDENTIFIER, "b"),
          new Node(Node.RIGHT_PAREN, null),
          new Node(Node.LEFT_BRACE, null),
          new Node(Node.RETURN_STATEMENT, new Node(Node.BINARY_OPERATOR, "+", new Node(Node.IDENTIFIER, "a"), new Node(Node.IDENTIFIER, "b"))),
          new Node(Node.RIGHT_BRACE, null)
        }),
        new Node(Node.IDENTIFIER, "add")
    });

    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
    FunctionParamType paramType1 = functionParamBuilder.withName("a").withType(INT).build();
    FunctionParamType paramType2 = functionParamBuilder.withName("b").withType(INT).build();

    FunctionType expectedFunctionType = new FunctionType(registry, functionName, sourceNode,
        new ArrowType(registry, new Node[] { paramType1, paramType2 }, INT.getInstance()), null, null, false, false);

    FunctionBuilder actualFunctionBuilder = functionBuilder
        .withName(functionName)
        .withSourceNode(sourceNode)
        .withParams(functionParamBuilder.withName("a").withType(INT).withName("b").withType(INT).build())
        .withReturnType(INT)
        .build();

    FunctionType actualFunctionType = actualFunctionBuilder.build();

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @org.junit.Test
  public void testFunctionBuilderWithVoidReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "logMessage";
    Node sourceNode = new Node(1, new Node[] {
        new Node(1, new Node[] {
          new Node(Node.IDENTIFIER, "function"),
          new Node(Node.IDENTIFIER, functionName),
          new Node(Node.LEFT_PAREN, null),
          new Node(Node.IDENTIFIER, "message"),
          new Node(Node.RIGHT_PAREN, null),
          new Node(Node.LEFT_BRACE, null),
          new Node(Node.STATEMENT_LIST, new Node[] {
            new Node(1, new Node[] {
              new Node(Node.PRINT_STATEMENT, new Node(Node.STRING_LITERAL, "Message: "), null),
              new Node(Node.EXPRESSION_STATEMENT, new Node(Node.ASSIGNMENT_OPERATOR, "console.log", new Node(Node.IDENTIFIER, "message")), null)
            }),
            new Node(Node.RETURN_STATEMENT, null)
          }),
          new Node(Node.RIGHT_BRACE, null)
        }),
        new Node(Node.IDENTIFIER, functionName)
    });

    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
    FunctionParamType paramType1 = functionParamBuilder.withName("message").withType(STRING).build();

    FunctionType expectedFunctionType = new FunctionType(registry, functionName, sourceNode,
        new ArrowType(registry, new Node[] { paramType1 }, VOID, true), null, null, false, false);

    FunctionBuilder actualFunctionBuilder = functionBuilder
        .withName(functionName)
        .withSourceNode(sourceNode)
        .withParams(functionParamBuilder.withName("message").withType(STRING).build())
        .withReturnType(VOID)
        .build();

    FunctionType actualFunctionType = actualFunctionBuilder.build();

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @org.junit.Test
  public void testFunctionBuilderWithErrorHandling() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "calculate";
    Node sourceNode = new Node(1, new Node[] {
        new Node(1, new Node[] {
          new Node(Node.IDENTIFIER, "function"),
          new Node(Node.IDENTIFIER, functionName),
          new Node(Node.LEFT_PAREN, null),
          new Node(Node.IDENTIFIER, "a"),
          new Node(Node.COMMA),
          new Node(Node.IDENTIFIER, "b"),
          new Node(Node.RIGHT_PAREN, null),
          new Node(Node.LEFT_BRACE, null),
          new Node(Node.TRY_STATEMENT, null, null),
          new Node(Node.CATCHEXCEPTIONStatement, null, new Node(Node.IDENTIFIER, "e"), null),
          new Node(Node.STATEMENT_LIST, new Node[] {
            new Node(Node.ASSIGNMENT_OPERATOR, "result", new Node(Node.BINARY_OPERATOR, "+", new Node(Node.IDENTIFIER, "a"), new Node(Node.IDENTIFIER, "b"))),
            new Node(Node.RETURN_STATEMENT, new Node(Node.IDENTIFIER, "result"))
          }),
          new Node(Node.FINALLY_STATEMENT, null, null),
          new Node(Node.THROW_STATEMENT, new Node(Node.NEW_EXPRESSION, new Node(Node.IDENTIFIER, "Error"), new Node[] {
            new Node(Node.STRING_LITERAL, "Error calculating.")
          }), null)
        }),
        new Node(Node.IDENTIFIER, functionName)
    });

    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
    FunctionParamType paramType1 = functionParamBuilder.withName("a").withType(INT).build();
    FunctionParamType paramType2 = functionParamBuilder.withName("b").withType(INT).build();

    FunctionType expectedFunctionType = new FunctionType(registry, functionName, sourceNode,
        new ArrowType(registry, new Node[] { paramType1, paramType2 }, INT.getInstance()), null, null, false, false);

    FunctionBuilder actualFunctionBuilder = functionBuilder
        .withName(functionName)
        .withSourceNode(sourceNode)
        .withParams(functionParamBuilder.withName("a").withType(INT).withName("b").withType(INT).build())
        .withReturnType(INT)
        .build();

    FunctionType actualFunctionType = actualFunctionBuilder.build();

    assertEquals(expectedFunctionType, actualFunctionType);
  }

  @org.junit.Test
  public void testFunctionBuilderWithTemplateType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    FunctionBuilder functionBuilder = new FunctionBuilder(registry);

    String functionName = "map";
    Node sourceNode = new Node(1, new Node[] {
        new Node(1, new Node[] {
          new Node(Node.IDENTIFIER, "function"),
          new Node(Node.IDENTIFIER, functionName),
          new Node(Node.LEFT_PAREN, null),
          new Node(Node.IDENTIFIER, "callback"),
          new Node(Node.COMMA),
          new Node(Node.IDENTIFIER, "array"),
          new Node(Node.RIGHT_PAREN, null),
          new Node(Node.LEFT_BRACE, null),
          new Node(Node.STATEMENT_LIST, new Node[] {
            new Node(1, new Node[] {
              new Node(Node.FOR_STATEMENT, new Node(Node.IDENTIFIER, "i"), new Node(Node.NUMBER_LITERAL, "0"), new Node(Node.BINARY_OPERATOR, "<=", new Node(Node.IDENTIFIER, "i"), new Node(Node.BINARY_OPERATOR, "%", new Node(Node.IDENTIFIER, "array"), new Node(Node.NUMBER_LITERAL, "length")))),
              new Node(Node.ASSIGNMENT_OPERATOR, new Node(Node.IDENTIFIER, "array"), new Node(Node.BINARY_OPERATOR, "[]", new Node(Node.IDENTIFIER, "array"))),
              new Node(Node.ASSIGNMENT_OPERATOR, new Node(Node.INDEX_EXPRESSION, new Node(Node.IDENTIFIER, "array"), new Node(Node.BINARY_OPERATOR, "+", new Node(Node.IDENTIFIER, "i"), new Node(Node.NUMBER_LITERAL, "1"))), new Node(Node.BINARY_OPERATOR, "=", new Node(Node.CALL_EXPRESSION, new Node(Node.IDENTIFIER, "callback"), new Node[] {
                new Node(Node.IDENTIFIER, "array"),
                new Node(Node.INDEX_EXPRESSION, new Node(Node.IDENTIFIER, "array"), new Node(Node.BINARY_OPERATOR, "+", new Node(Node.IDENTIFIER, "i"), new Node(Node.NUMBER_LITERAL, "1")))
              })))
            }),
            new Node(Node.RETURN_STATEMENT, new Node(Node.IDENTIFIER, "array"))
          }),
          new Node(Node.RIGHT_BRACE, null)
        }),
        new Node(Node.IDENTIFIER, functionName)
    });

    FunctionParamBuilder functionParamBuilder = new FunctionParamBuilder(registry);
    FunctionParamType paramType1 = functionParamBuilder.withName("callback").withType(FunctionType.createFunctionWithVoidReturnType(null, new Node[] { new ObjectType( registry, "any" ) }, new Node[] { new ObjectType( registry, "any" ), new ArrayType(registry, INT.getInstance()) })).build();
    FunctionParamType paramType2 = functionParamBuilder.withName("array").withType(new ArrayType(registry, INT.getInstance())).build();

    FunctionType expectedFunctionType = new FunctionType(registry, functionName, sourceNode,
        new ArrowType(registry, new Node[] { paramType1, paramType2 }, new ArrayType(registry, INT.getInstance()), true), null, null, false, false);

    expectedFunctionType.setTemplateTypeName("Function<any, any[], any[]>");

    FunctionBuilder actualFunctionBuilder = functionBuilder
        .withName(functionName)
        .withSourceNode(sourceNode)
        .withParams(functionParamBuilder.withName("callback").withType(FunctionType.createFunctionWithVoidReturnType(null, new Node[] { new ObjectType( registry, "any" ) }, new Node[] { new ObjectType( registry, "any" ), new ArrayType(registry, INT.getInstance()) })).build(),
            functionParamBuilder.withName("array").withType(new ArrayType(registry, INT.getInstance())).build())
        .withReturnType(new ArrayType(registry, INT.getInstance()))
        .build();

    FunctionType actualFunctionType = actualFunctionBuilder.build();

    assertEquals(expectedFunctionType, actualFunctionType);
  }
}