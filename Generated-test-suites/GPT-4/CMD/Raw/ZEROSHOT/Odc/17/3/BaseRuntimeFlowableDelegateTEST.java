
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.ArgumentCaptor;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.common.engine.impl.de.odysseus.el.ValueExpressionImpl;
import org.flowable.el.juel.TreeBuilder;
import org.flowable.el.juel.BuilderContext;
import org.flowable.variable.service.impl.el.NoExecutionVariableScope;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RunWith(MockitoJUnitRunner.class)
public class BaseRuntimeFlowableDelegateTest {
    @Mock
    private DelegateExecution execution;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private BaseRuntimeFlowableDelegate<Object> delegate;

    @Test
    public void testExecute() throws Exception {
        doNothing().when(execution).setVariable(anyString(), any());
        delegate.execute(execution);
        verify(eventPublisher, times(2)).removeEventListener(any());
    }

    @Test
    public void testRun() throws Exception {
        BuilderContext context = new BuilderContext(TreeBuilder.DEFAULT);
        ValueExpressionImpl ve = new ValueExpressionImpl(context, "1+1", Integer.class);
        when(execution.getVariable(anyString())).thenReturn(ve);
        Callable<Object> callable = () -> new Object();
        delegate.initCallable(execution);
        delegate.run(execution);
        verify(execution).getCurrentActivityId();
    }

    @Test(expected = ExecutionException.class)
    public void testGetAfterTaskExecutionFailed() throws Exception {
        BuilderContext context = new BuilderContext(TreeBuilder.DEFAULT);
        ValueExpressionImpl ve = new ValueExpressionImpl(context, "1+1", Integer.class);
        when(execution.getVariable(anyString())).thenReturn(ve);
        delegate.run(execution);
        delegate.get();
    }

    @Test
    public void testIsDone() throws Exception {
        BuilderContext context = new BuilderContext(TreeBuilder.DEFAULT);
        ValueExpressionImpl ve = new ValueExpressionImpl(context, "1+1", Integer.class);
        when(execution.getVariable(anyString())).thenReturn(ve);
        delegate.run(execution);
        assertTrue(delegate.isDone());
    }

    @Test(expected = ExecutionException.class)
    public void testGetWithTimeoutAfterTaskExecutionFailed() throws Exception {
        BuilderContext context = new BuilderContext(TreeBuilder.DEFAULT);
        ValueExpressionImpl ve = new ValueExpressionImpl(context, "1+1", Integer.class);
        when(execution.getVariable(anyString())).thenReturn(ve);
        delegate.run(execution);
        delegate.get(1, TimeUnit.SECONDS);
    }
}
