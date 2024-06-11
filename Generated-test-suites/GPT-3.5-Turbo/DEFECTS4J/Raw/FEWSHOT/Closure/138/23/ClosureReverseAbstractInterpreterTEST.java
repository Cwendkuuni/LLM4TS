package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.Visitor;
import com.google.javascript.rhino.Node;

import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ClosureReverseAbstractInterpreterTest {
  
  @Mock
  private JSTypeRegistry mockTypeRegistry;
  
  private ClosureReverseAbstractInterpreter closureInterpreter;
  
  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    closureInterpreter = new ClosureReverseAbstractInterpreter(mockTypeRegistry);
  }
  
  @Test
  public void testGetPreciserScopeKnowingConditionOutcome_returnsNextPreciserScopeKnowingConditionOutcome() {
    Node condition = mock(Node.class);
    FlowScope blindScope = mock(FlowScope.class);
    boolean outcome = true;
    
    FlowScope result = closureInterpreter.getPreciserScopeKnowingConditionOutcome(condition, blindScope, outcome);
    
    verifyZeroInteractions(condition);
    verifyZeroInteractions(blindScope);
    verifyZeroInteractions(mockTypeRegistry);
    assertNull(result);
  }
  
  @Test
  public void testRestrictParameter_restrictsParameterType() {
    Node parameter = mock(Node.class);
    JSType type = mock(JSType.class);
    FlowScope blindScope = mock(FlowScope.class);
    Function<TypeRestriction, JSType> restriction = mock(Function.class);
    boolean outcome = true;
    JSType restrictedType = mock(JSType.class);
    FlowScope informedScope = mock(FlowScope.class);
    when(restriction.apply(any(TypeRestriction.class))).thenReturn(restrictedType);
    when(blindScope.createChildFlowScope()).thenReturn(informedScope);
    
    FlowScope result = closureInterpreter.restrictParameter(parameter, type, blindScope, restriction, outcome);
    
    verify(parameter).isQualifiedName();
    verify(restriction).apply(any(TypeRestriction.class));
    verify(informedScope).declareNameInScope(parameter, restrictedType);
    assertEquals(informedScope, result);
  }
  

}