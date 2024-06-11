import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class CheckGlobalThisTest {
  
  private AbstractCompiler compilerMock;
  private CheckLevel levelMock;
  private NodeTraversal tMock;
  private Node nMock;
  private Node parentMock;
  private JSDocInfo jsDocMock;
  
  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setUp() {
    compilerMock = mock(AbstractCompiler.class);
    levelMock = mock(CheckLevel.class);
    tMock = mock(NodeTraversal.class);
    nMock = mock(Node.class);
    parentMock = mock(Node.class);
    jsDocMock = mock(JSDocInfo.class);
    
    checkGlobalThis = new CheckGlobalThis(compilerMock, levelMock);
  }
  
  @Test
  public void shouldReportThis_whenThisOnLeftHandSideOfAssignment() {
    when(nMock.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(nMock, parentMock)).thenReturn(true);
    
    checkGlobalThis.visit(tMock, nMock, parentMock);
    
    verify(compilerMock, times(1)).report(tMock.makeError(nMock, levelMock, CheckGlobalThis.GLOBAL_THIS));
  }
  
  @Test
  public void shouldReportThis_whenThisWithPropertyAccess() {
    when(nMock.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(nMock, parentMock)).thenReturn(true);
    
    checkGlobalThis.visit(tMock, nMock, parentMock);
    
    verify(compilerMock, times(1)).report(tMock.makeError(nMock, levelMock, CheckGlobalThis.GLOBAL_THIS));
  }
  
  @Test
  public void shouldNotReportThis_whenThisOnLeftHandSideOfAssignmentIsNotSafe() {
    when(nMock.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(nMock, parentMock)).thenReturn(false);
    
    checkGlobalThis.visit(tMock, nMock, parentMock);
    
    verify(compilerMock, times(0)).report(any());
  }
  
  @Test
  public void shouldNotReportThis_whenThisWithPropertyAccessIsNotSafe() {
    when(nMock.getType()).thenReturn(Token.THIS);
    when(checkGlobalThis.shouldReportThis(nMock, parentMock)).thenReturn(false);
    
    checkGlobalThis.visit(tMock, nMock, parentMock);
    
    verify(compilerMock, times(0)).report(any());
  }
}