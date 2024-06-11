package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.rhino.Node;

/**
 * JUnit test cases for the PeepholeOptimizationsPass class.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(PeepholeOptimizationsPass.class)
public class PeepholeOptimizationsPassTest {
  private AbstractCompiler compiler;
  private PeepholeOptimizationsPass pass;

  @Before
  public void setUp() {
    compiler = PowerMockito.mock(AbstractCompiler.class);
    AbstractPeepholeOptimization[] optimizations = new AbstractPeepholeOptimization[0];
    pass = new PeepholeOptimizationsPass(compiler, optimizations);
  }

  @Test
  public void testProcess() {
    Node root = PowerMockito.mock(Node.class);
    Node externs = PowerMockito.mock(Node.class);

    pass.process(externs, root);

    // Basic functionality test for process() method.
    // Add more assertions based on the class behavior.
    verify(compiler, times(1)).addChangeHandler(pass.new PeepholeChangeHandler());
    verify(compiler, times(1)).removeChangeHandler(pass.new PeepholeChangeHandler());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcessNullCompiler() {
    AbstractCompiler nullCompiler = null;
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(nullCompiler, new AbstractPeepholeOptimization[0]);

    Node root = PowerMockito.mock(Node.class);
    Node externs = PowerMockito.mock(Node.class);

    pass.process(externs, root);

    // Test for exception handling scenario.
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcessNullOptimizations() {
    AbstractCompiler compiler = PowerMockito.mock(AbstractCompiler.class);
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, null);

    Node root = PowerMockito.mock(Node.class);
    Node externs = PowerMockito.mock(Node.class);

    pass.process(externs, root);

    // Test for exception handling scenario.
  }
}