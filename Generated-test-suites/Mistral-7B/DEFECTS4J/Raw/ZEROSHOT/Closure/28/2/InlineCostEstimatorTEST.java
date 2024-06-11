import static org.junit.Assert.*;
import static com.google.javascript.jscomp.InlineCostEstimator.*;
import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class InlineCostEstimatorTest {

 private InlineCostEstimator estimator;
 private Node root;
 private int expectedCost;

 public InlineCostEstimatorTest(Node root, int expectedCost) {
 this.root = root;
 this.expectedCost = expectedCost;
 }

 @Parameterized.Parameters(name = "{index}: getCost({0}) should be {1}")
 public static Collection<Object[]> data() {
 return ImmutableList.of(
 new Object[] {
 NodeOperations.createLiteral(true), 0
 },
 new Object[] {
 NodeOperations.createLiteral(false), 0
 },
 new Object[] {
 NodeOperations.createNullLiteral(), 0
 },
 new Object[] {
 NodeOperations.createStringLiteral(""), 0
 },
 new Object[] {
 NodeOperations.createThis(), 1
 },
 new Object[] {
 NodeOperations.createIdentifier("a"), 2
 },
 new Object[] {
 NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")), 4
 },
 new Object[] {
 NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b"))), 4
 },
 new Object[] {
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))), 1
 },
 new Object[] {
 NodeOperations.createBlock(ImmutableList.of(NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))))), 1
 },
 new Object[] {
 NodeOperations.createBlock(ImmutableList.of(
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))))), 2
 },
 new Object[] {
 NodeOperations.createBlock(ImmutableList.of(
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))))), 3
 },
 new Object[] {
 NodeOperations.createBlock(ImmutableList.of(
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))))), 4
 },
 new Object[] {
 NodeOperations.createBlock(ImmutableList.of(
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))))), 5
 },
 new Object[] {
 NodeOperations.createBlock(ImmutableList.of(
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))))), 6
 },
 new Object[] {
 NodeOperations.createBlock(ImmutableList.of(
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))))), 7
 },
 new Object[] {
 NodeOperations.createBlock(ImmutableList.of(
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))))), 8
 },
 new Object[] {
 NodeOperations.createBlock(ImmutableList.of(
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))),
 NodeOperations.createStatement(NodeOperations.createAssignment(NodeOperations.createIdentifier("a"), NodeOperations.createBinaryOperator(NodeOperations.createIdentifier("a"), NodeOperations.OP_PLUS, NodeOperations.createIdentifier("b")))))), 9
 }
 );
 }

 @Test
 public void testGetCost() {
 estimator = new InlineCostEstimator();
 int actualCost = getCost(root);
 assertEquals("getCost() should return the expected cost.", expectedCost, actualCost);
 }
}