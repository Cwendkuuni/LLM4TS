import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.LinkedHashMap;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticScope.StaticSlot;
import com.google.javascript.rhino.jstype.StaticSlot.SlotType;

import java.util.Iterator;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class ScopeTest {
  private Scope scope;
  private ErrorReporter errorReporter;
  private Node rootNode;

  @Before
  public void setUp() {
    errorReporter = mock(ErrorReporter.class);
    rootNode = mock(Node.class);
    scope = new Scope(null, rootNode) {
      @Override
      public void reportError(Node node, int line, String msg, String detail) {
        errorReporter.reportError(0, 0, msg, node, line);
      }
    };
  }

  @Test
  public void testCreateScope() {
    assertEquals(0, scope.getDepth());
    assertFalse(scope.isBottom());
    assertNull(scope.thisType);
  }

  @Test
  public void testCreateLocalScope() {
    FunctionType functionType = mock(FunctionType.class);
    when(functionType.getTypeOfThis()).thenReturn(mock(ObjectType.class));
    ObjectType thisType = mock(ObjectType.class);
    when(thisType.getName()).thenReturn("Object");
    when(rootNode.getJSType()).thenReturn(functionType);

    Scope parentScope = mock(Scope.class);
    Scope localScope = new Scope(parentScope, rootNode);
    assertEquals(1, localScope.getDepth());
    assertEquals("Object", localScope.thisType.getName());
    assertFalse(localScope.isBottom());
  }

  @Test
  public void testCreateGlobalScope() {
    Scope globalScope = new Scope(null, null);
    assertEquals(0, globalScope.getDepth());
    assertFalse(globalScope.isBottom());
    assertSame(GlobalObject.getObjectType(null), globalScope.thisType);
  }

  @Test
  public void testCreateEmptyScope() {
    Scope emptyScope = new Scope(null, null);
    assertEquals(0, emptyScope.getDepth());
    assertTrue(emptyScope.isBottom());
  }

  @Test
  public void testIsDeclared() {
    Scope scope = new Scope(null, null);
    Var var1 = scope.declare("var1", null, JSType.BOOLEAN, null);
    assertTrue(scope.isDeclared("var1", false));
    assertFalse(scope.isDeclared("var2", false));

    Scope parentScope = mock(Scope.class);
    when(parentScope.isDeclared("var1", true)).thenReturn(true);
    scope = new Scope(parentScope, null);
    assertTrue(scope.isDeclared("var1", false));
    assertTrue(scope.isDeclared("var1", true));
  }

  @Test
  public void testGetVar() {
    Var var1 = scope.declare("var1", null, JSType.BOOLEAN, null);
    Var var2 = scope.declare("var2", null, JSType.NUMBER, null);

    assertSame(var1, scope.getVar("var1"));
    assertSame(var2, scope.getVar("var2"));
    assertNull(scope.getVar("var3"));
  }

  @Test
  public void testGetSlot() {
    Var var1 = scope.declare("var1", null, JSType.BOOLEAN, null);

    StaticSlot<JSType> slot = scope.getSlot("var1");
    assertEquals(var1, slot);
  }

  @Test(expected = NullPointerException.class)
  public void testGetSlotNullName() {
    scope.getSlot(null);
  }

  @Test
  public void testDeclare() {
    Var var1 = scope.declare("var1", null, JSType.BOOLEAN, null);
    assertEquals("var1", var1.getName());
    assertEquals(JSType.BOOLEAN, var1.getType());
    assertEquals(null, var1.getParentNode());
    assertFalse(var1.isBleedingFunction());
    assertFalse(var1.isGlobal());
    assertFalse(var1.isLocal());
    assertFalse(var1.isExtern());
    assertFalse(var1.isConst());
    assertFalse(var1.isDefine());
    assertFalse(var1.isNoShadow());

    Var var2 = scope.declare("var2", null, JSType.NUMBER, null);
    assertEquals("var2", var2.getName());
    assertEquals(JSType.NUMBER, var2.getType());
    assertFalse(var2.isBleedingFunction());
    assertFalse(var2.isGlobal());
    assertFalse(var2.isLocal());
    assertFalse(var2.isExtern());
    assertFalse(var2.isConst());
    assertFalse(var2.isDefine());
    assertFalse(var2.isNoShadow());

    Var var3 = scope.declare("var3", null, JSType.STRING, null);
    assertEquals("var3", var3.getName());
    assertEquals(JSType.STRING, var3.getType());
    assertFalse(var3.isBleedingFunction());
    assertFalse(var3.isGlobal());
    assertFalse(var3.isLocal());
    assertFalse(var3.isExtern());
    assertFalse(var3.isConst());
    assertFalse(var3.isDefine());
    assertFalse(var3.isNoShadow());
  }

  @Test
  public void testDeclareWithJSDocInfo() {
    JSDocInfo jsdocInfo = mock(JSDocInfo.class);
    when(jsdocInfo.getName()).thenReturn("myName");
    when(jsdocInfo.isDefine()).thenReturn(true);

    Var var = scope.declare("var1", null, JSType.BOOLEAN, null, jsdocInfo);
    assertEquals("myName", var.getName());
    assertTrue(var.isDefine());
  }

  @Test
  public void testDeclareWithInput() {
    CompilerInput compilerInput = mock(CompilerInput.class);
    when(compilerInput.getName()).thenReturn("myInput");

    Var var = scope.declare("var1", null, JSType.BOOLEAN, null, null, compilerInput);
    assertEquals("myInput", var.getInputName());
  }

  @Test
  public void testUndeclare() {
    Var var = scope.declare("var1", null, JSType.BOOLEAN, null);
    scope.undeclare(var);
    assertNull(scope.getVar("var1"));
  }

  @Test
  public void testGetVars() {
    Var var1 = scope.declare("var1", null, JSType.BOOLEAN, null);
    Var var2 = scope.declare("var2", null, JSType.NUMBER, null);

    Iterator<Var> iter = scope.getVars();
    assertTrue(iter.hasNext());
    Var var = iter.next();
    assertEquals(var1, var);
    assertTrue(iter.hasNext());
    var = iter.next();
    assertEquals(var2, var);
    assertFalse(iter.hasNext());
  }

  @Test
  public void testGetVarCount() {
    scope.declare("var1", null, JSType.BOOLEAN, null);
    scope.declare("var2", null, JSType.NUMBER, null);
    assertEquals(2, scope.getVarCount());
  }

  @Test
  public void testIsLocal() {
    Scope scope = new Scope(null, null);
    assertFalse(scope.isLocal());

    Scope parentScope = mock(Scope.class);
    when(parentScope.isLocal()).thenReturn(false);
    scope = new Scope(parentScope, null);
    assertFalse(scope.isLocal());

    scope = new Scope(null, null) {
      @Override
      public boolean isLocal() {
        return true;
      }
    };
    assertTrue(scope.isLocal());
  }

  @Test
  public void testIsGlobal() {
    Scope scope = new Scope(null, null);
    assertFalse(scope.isGlobal());

    Scope parentScope = mock(Scope.class);
    when(parentScope.isGlobal()).thenReturn(false);
    scope = new Scope(parentScope, null);
    assertFalse(scope.isGlobal());

    scope = new Scope(null, null) {
      @Override
      public boolean isGlobal() {
        return true;
      }
    };
    assertTrue(scope.isGlobal());
  }

  @Test
  public void testIsBottom() {
    Scope scope = new Scope(null, null);
    assertFalse(scope.isBottom());

    Scope parentScope = mock(Scope.class);
    when(parentScope.isBottom()).thenReturn(false);
    scope = new Scope(parentScope, null);
    assertFalse(scope.isBottom());

    scope = new Scope(null, null) {
      @Override
      public boolean isBottom() {
        return true;
      }
    };
    assertTrue(scope.isBottom());
  }

  @Test
  public void testJoin() {
    Scope leftScope = new Scope(null, null) {
      @Override
      public StaticSlot<JSType> getSlot(String name) {
        if (name.equals("a")) {
          return new StaticSlot<JSType>() {
            @Override
            public String getName() {
              return "a";
            }

            @Override
            public SlotType getType() {
              return SlotType.VAR;
            }

            @Override
            public JSType getValue() {
              return JSType.BOOLEAN;
            }

            @Override
            public void setValue(JSType value) {
              // empty
            }
          };
        } else {
          return null;
        }
      }
    };

    Scope rightScope = new Scope(null, null) {
      @Override
      public StaticSlot<JSType> getSlot(String name) {
        if (name.equals("a")) {
          return new StaticSlot<JSType>() {
            @Override
            public String getName() {
              return "a";
            }

            @Override
            public SlotType getType() {
              return SlotType.VAR;
            }

            @Override
            public JSType getValue() {
              return JSType.NUMBER;
            }

            @Override
            public void setValue(JSType value) {
              // empty
            }
          };
        } else {
          return null;
        }
      }
    };

    StaticScope<JSType> joinedScope = new LinkedHashMap<String, StaticSlot<JSType>>() {
      @Override
      public StaticSlot<JSType> getSlot(String name) {
        StaticScope<JSType> scope = this;
        while (scope != null) {
          StaticSlot<JSType> slot = scope.getSlot(name);
          if (slot != null) {
            return slot;
          }
          scope = (StaticScope<JSType>) scope.getParentScope();
        }
        return null;
      }
    }.join(leftScope).join(rightScope);

    StaticSlot<JSType> slot = joinedScope.getSlot("a");
    assertEquals(JSType.TYPE_UNION, slot.getValue().getType());
    JSType leftType = slot.getValue().getMemberType("left");
    assertEquals(JSType.BOOLEAN, leftType);
    JSType rightType = slot.getValue().getMemberType("right");
    assertEquals(JSType.NUMBER, rightType);
  }
}