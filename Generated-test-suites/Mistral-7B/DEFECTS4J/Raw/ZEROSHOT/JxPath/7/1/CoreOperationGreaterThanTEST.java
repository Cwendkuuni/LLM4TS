package org.apache.commons.jxpath.ri.compiler;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.NodeInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class CoreOperationGreaterThanTest {

 private CoreOperationGreaterThan operation;
 @Mock private EvalContext context;
 @Mock private Expression arg1;
 @Mock private Expression arg2;
 @Mock private NodeInfo nodeInfo;
 @Mock private InfoSet infoSet;

 @Before
 public void setUp() {
 InfoSetUtil.setNodeInfoAndInfoSet(nodeInfo, infoSet);
 Mockito.when(arg1.computeValue(context)).thenReturn(Double.valueOf(5.0));
 Mockito.when(arg2.computeValue(context)).thenReturn(Double.valueOf(3.0));
 operation = new CoreOperationGreaterThan(arg1, arg2);
 }

 @Test
 public void testComputeValueTrue() {
 assertEquals(Boolean.TRUE, operation.computeValue(context));
 }

 @Test
 public void testComputeValueFalse() {
 InfoSetUtil.setNodeInfoAndInfoSet(nodeInfo, infoSet);
 Mockito.when(arg1.computeValue(context)).thenReturn(Double.valueOf(3.0));
 assertEquals(Boolean.FALSE, operation.computeValue(context));
 }

 @Test
 public void testGetSymbol() {
 assertEquals(">", operation.getSymbol());
 }
}