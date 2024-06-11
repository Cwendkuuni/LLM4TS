import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowFunction;
import static com.google.javascript.rhino.NodeFactory.createBooleanLiteral;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createIntLiteral;
import static com.google.javascript.rhino.NodeFactory.createNamedProperty;
import static com.google.javascript.rhino.NodeFactory.createObjectLiteral;
import static com.google.javascript.rhino.NodeFactory.createStringLiteral;
import static com.google.javascript.rhino.NativeType.BOOLEAN;
import static com.google.javascript.rhino.NativeType.FUNCTION;
import static com.google.javascript.rhino.NativeType.INT;
import static com.google.javascript.rhino.NativeType.OBJECT;
import static com.google.javascript.rhino.NativeType.STRING;
import static com.google.javascript.rhino.NodeFactory.createTypeOf;
import static com.google.javascript.rhino.JSTypeFactory.getNativeType;
import static com.google.javascript.rhino.JSTypeFactory.makeArrowType;
import static com.google.javascript.rhino.JSTypeFactory.makeFunctionType;
import static com.google.javascript.rhino.JSTypeFactory.makeParameterList;
import static com.google.javascript.rhino.JSTypeFactory.makeTypeParameter;
import static com.google.javascript.rhino.JSTypeFactory.makeUnknownType;
import static com.google.javascript.rhino.JSTypeFactory.makeVoidType;
import static com.google.javascript.rhino.JSTypeFactory.makeTypeUnion;
import static com.google.javascript.rhino.JSTypeFactory.makeTypeIntersection;
import static com.google.javascript.rhino.JSTypeFactory.makeTypeLiteral;
import static com.google.javascript.rhino.JSTypeFactory.makeTypeParameterInstance;
import static com.google.javascript.rhino.JSTypeFactory.makeTypeVariable;
import static com.google.javascript.rhino.JSTypeFactory.makeTypeVariableWithBound;
import static com.google.javascript.rhino.JSTypeFactory.makeWildcardType;
import static com.google.javascript.rhino.JSTypeRegistry.getInstance;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSTypeNative.UNKNOWN_TYPE;
import com.google.javascript.rhino.testing.JSTestCase;

public class ArrowTypeTest extends JSTestCase {
  private static final JSTypeRegistry registry = getInstance();

  public void testArrowTypeConstruction() {
    Node parameters = createParameterList(makeTypeParameter("T"));
    Node returnType = createIntLiteral(5);
    Node functionNode = createArrowFunction("x", parameters, returnType);

    ArrowType arrowType = new ArrowType(registry, parameters, returnType, false);
    assertEquals(arrowType.toString(), functionNode.toString());

    JSType arrowTypeWithInferredReturn = new ArrowType(registry, parameters, null, true);
    assertEquals(arrowTypeWithInferredReturn.toString(), functionNode.toString());
  }

  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = getInstance();
    JSType returnType1 = makeIntType(registry);
    JSType returnType2 = makeIntType(registry);
    JSType paramType1 = makeAnyType();
    JSType paramType2 = makeIntType(registry);

    ArrowType arrowType1 = new ArrowType(registry, createParameterList(), returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry, createParameterList(paramType2), returnType2, false);

    assertTrue(arrowType1.isSubtype(arrowType2));
    assertFalse(arrowType2.isSubtype(arrowType1));

    JSType arrowTypeWithInferredReturn1 = new ArrowType(registry, createParameterList(), returnType1, true);
    JSType arrowTypeWithInferredReturn2 = new ArrowType(registry, createParameterList(paramType2), returnType2, true);

    assertTrue(arrowTypeWithInferredReturn1.isSubtype(arrowTypeWithInferredReturn2));
    assertFalse(arrowTypeWithInferredReturn2.isSubtype(arrowTypeWithInferredReturn1));

    ArrowType arrowTypeWithVarArgs1 = new ArrowType(registry, createParameterList(createParameterList(), createTypeVariableWithBound(registry, "T")), returnType1, false);
    ArrowType arrowTypeWithVarArgs2 = new ArrowType(registry, createParameterList(createParameterList(), createTypeVariableWithBound(registry, "S")), returnType1, false);

    assertTrue(arrowTypeWithVarArgs1.isSubtype(arrowTypeWithVarArgs2));
  }

  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = getInstance();

    JSType paramType1 = makeIntType(registry);
    JSType paramType2 = makeStringType(registry);

    ArrowType arrowType1 = new ArrowType(registry, createParameterList(paramType1), makeIntType(registry), false);
    ArrowType arrowType2 = new ArrowType(registry, createParameterList(paramType2), makeIntType(registry), false);

    assertFalse(arrowType1.hasEqualParameters(arrowType2));

    ArrowType arrowType3 = new ArrowType(registry, createParameterList(paramType1), makeIntType(registry), false);
    ArrowType arrowType4 = new ArrowType(registry, createParameterList(paramType1), makeIntType(registry), false);

    assertTrue(arrowType3.hasEqualParameters(arrowType4));

    ArrowType arrowType5 = new ArrowType(registry, createParameterList(paramType1), makeIntType(registry), false);
    ArrowType arrowType6 = new ArrowType(registry, createParameterList(createParameterList(paramType1), paramType1), makeIntType(registry), false);

    assertTrue(arrowType5.hasEqualParameters(arrowType6));
  }

  public void testArrowTypeEquivalence() {
    JSTypeRegistry registry = getInstance();

    JSType returnType1 = makeIntType(registry);
    JSType returnType2 = makeIntType(registry);
    JSType paramType1 = makeIntType(registry);
    JSType paramType2 = makeStringType(registry);

    ArrowType arrowType1 = new ArrowType(registry, createParameterList(paramType1), returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry, createParameterList(paramType1), returnType1, false);
    ArrowType arrowType3 = new ArrowType(registry, createParameterList(paramType2), returnType1, false);

    assertTrue(arrowType1.equals(arrowType2));
    assertFalse(arrowType1.equals(arrowType3));

    ArrowType arrowType4 = new ArrowType(registry, createParameterList(createTypeVariableWithBound(registry, "T")), makeIntType(registry), false);
    ArrowType arrowType5 = new ArrowType(registry, createParameterList(createTypeVariableWithBound(registry, "S")), makeIntType(registry), false);

    assertFalse(arrowType4.equals(arrowType5));
  }

  public void testArrowTypeHashCode() {
    JSTypeRegistry registry = getInstance();

    JSType returnType1 = makeIntType(registry);
    JSType returnType2 = makeIntType(registry);
    JSType paramType1 = makeIntType(registry);
    JSType paramType2 = makeStringType(registry);

    ArrowType arrowType1 = new ArrowType(registry, createParameterList(paramType1), returnType1, false);
    ArrowType arrowType2 = new ArrowType(registry, createParameterList(paramType1), returnType1, false);
    ArrowType arrowType3 = new ArrowType(registry, createParameterList(paramType2), returnType1, false);

    assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
    assertNotEquals(arrowType1.hashCode(), arrowType3.hashCode());

    ArrowType arrowType4 = new ArrowType(registry, createParameterList(createTypeVariableWithBound(registry, "T")), makeIntType(registry), false);
    ArrowType arrowType5 = new ArrowType(registry, createParameterList(createTypeVariableWithBound(registry, "S")), makeIntType(registry), false);

    assertNotEquals(arrowType4.hashCode(), arrowType5.hashCode());
  }

  // Add more test cases for other methods here...

  private Node createParameterList(JSType... paramTypes) {
    Node parameterList = createObjectLiteral();
    for (JSType paramType : paramTypes) {
      parameterList = createNamedProperty(parameterList, "type", paramType.toStringNode());
    }
    return parameterList;
  }

  private JSType makeIntType(JSTypeRegistry registry) {
    return registry.getIntType();
  }

  private JSType makeStringType(JSTypeRegistry registry) {
    return registry.getStringType();
  }

  private JSType makeBooleanType(JSTypeRegistry registry) {
    return registry.getBooleanType();
  }

  private JSType makeFunctionType(JSTypeRegistry registry, JSType returnType, JSType... parameterTypes) {
    return new FunctionType(registry, createParameterList(parameterTypes), returnType);
  }

  private JSType makeTypeVariable(JSTypeRegistry registry, String name) {
    return registry.getTypeVariable("T" + name);
  }

  private JSType makeTypeVariableWithBound(JSTypeRegistry registry, String name) {
    return registry.getTypeVariable("T" + name, getNativeType(INT));
  }

  private JSType makeAnyType() {
    return registry.getAnyType();
  }

  private JSType makeVoidType(JSTypeRegistry registry) {
    return registry.getVoidType();
  }

  private JSType makeArrowType(JSTypeRegistry registry, Node functionNode) {
    Node parameters = functionNode.getParameters();
    return new ArrowType(registry, parameters, parseType(functionNode.getBody().getChild(0)), false);
  }

  private JSType makeTypeUnion(JSTypeRegistry registry, JSType... types) {
    return registry.getUnionType(types);
  }

  private JSType makeTypeIntersection(JSTypeRegistry registry, JSType... types) {
    return registry.getIntersectionType(types);
  }

  private JSType makeTypeLiteral(JSTypeRegistry registry, Class<?> clazz) {
    return registry.getTypeForClass(clazz);
  }

  private JSType makeTypeParameterInstance(JSTypeRegistry registry, JSType parameter) {
    return registry.makeInstance(registry.getTypeVariable("T"), parameter);
  }

  private JSType makeWildcardType(JSTypeRegistry registry, JSType bound) {
    return registry.getWildcardType(bound);
  }

  private JSType parseType(Node node) {
    return new Visitor<JSType>() {
      @Override
      public JSType visitArrowType(ArrowType arrowType) {
        return arrowType;
      }

      @Override
      public JSType visitFunctionType(FunctionType functionType) {
        return makeFunctionType(registry, parseType(node.getLastChild()), parseTypeList(functionType.getParameters()));
      }

      @Override
      public JSType visitIntType(IntType intType) {
        return makeIntType(registry);
      }

      @Override
      public JSType visitStringType(StringType stringType) {
        return makeStringType(registry);
      }

      @Override
      public JSType visitBooleanType(BooleanType booleanType) {
        return makeBooleanType(registry);
      }

      @Override
      public JSType visitVoidType(VoidType voidType) {
        return makeVoidType(registry);
      }

      @Override
      public JSType visitTypeUnion(TypeUnion typeUnion) {
        return makeTypeUnion(registry, parseTypes(typeUnion.getTypes()));
      }

      @Override
      public JSType visitTypeIntersection(TypeIntersection typeIntersection) {
        return makeTypeIntersection(registry, parseTypes(typeIntersection.getTypes()));
      }

      @Override
      public JSType visitTypeParameter(TypeParameter typeParameter) {
        return makeTypeVariable(registry, typeParameter.getName());
      }

      @Override
      public JSType visitTypeVariable(TypeVariable typeVariable) {
        return typeVariable;
      }

      @Override
      public JSType visitNativeType(NativeType nativeType) {
        return getNativeType(nativeType.getName());
      }

      @Override
      public JSType visitTypeLiteral(TypeLiteral typeLiteral) {
        return makeTypeLiteral(registry, typeLiteral.getClass());
      }

      @Override
      public JSType visitWildcardType(WildcardType wildcardType) {
        return makeWildcardType(registry, parseType(wildcardType.getBound()));
      }

      @Override
      public JSType visitUnknownType(UnknownType unknownType) {
        return makeUnknownType(registry);
      }

      @Override
      public JSType visitParameterList(ParameterList parameterList) {
        return parseTypeList(parameterList.getParameters());
      }

      @Override
      public JSType visitTypeParameterList(TypeParameterList typeParameterList) {
        JSType[] types = new JSType[typeParameterList.getLength()];
        for (int i = 0; i < types.length; i++) {
          types[i] = parseType(typeParameterList.getParameter(i));
        }
        return makeTypeParameterList(registry, types);
      }

      @Override
      public JSType visitTypeVariableWithBound(TypeVariableWithBound typeVariableWithBound) {
        return makeTypeVariableWithBound(registry, typeVariableWithBound.getName());
      }

      @Override
      public JSType visitTypeParameterInstance(TypeParameterInstance typeParameterInstance) {
        return makeTypeParameterInstance(registry, parseType(typeParameterInstance.getType()));
      }
    }.visit(node);
  }

  private JSType[] parseTypes(Node[] nodes) {
    JSType[] types = new JSType[nodes.length];
    for (int i = 0; i < nodes.length; i++) {
      types[i] = parseType(nodes[i]);
    }
    return types;
  }

  private JSType[] parseTypeList(Node node) {
    JSType[] types = new JSType[node.getLength()];
    for (int i = 0; i < types.length; i++) {
      types[i] = parseType(node.getChild(i));
    }
    return types;
  }
}