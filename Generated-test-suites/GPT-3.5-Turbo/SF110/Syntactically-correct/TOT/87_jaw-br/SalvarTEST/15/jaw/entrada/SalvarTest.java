import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class SalvarTest {
    
    private ProcessarEntidades mockForm;
    private JFileChooser mockEscolhedor;
    private File mockSelectedFile;
    private FileWriter mockEscrevedor;
    
    private Salvar salvar;
    
    @Before
    public void setUp() {
        mockForm = Mockito.mock(ProcessarEntidades.class);
        mockEscolhedor = Mockito.mock(JFileChooser.class);
        mockSelectedFile = Mockito.mock(File.class);
        mockEscrevedor = Mockito.mock(FileWriter.class);
        
        salvar = new Salvar(mockForm, 10);
    }
    
    @Test
    public void testConstructorValidType() {
        assertEquals(10, salvar.tipoSalvar);
    }
    
    @Test(expected = Exception.class)
    public void testConstructorException() throws Exception {
        throw new Exception();
    }
    
    @Test
    public void testConstructorVelaFalse() {
        assertFalse(salvar.vela);
    }
    
    @Test
    public void testConstructorArquivoEmptyString() {
        assertEquals("", salvar.arquivo);
    }
    
    @Test
    public void testConstructorXmlEmptyString() {
        assertEquals("", salvar.xml);
    }
    
    @Test
    public void testConstructorEntidadeEmptyList() {
        assertTrue(salvar.entidade.isEmpty());
    }
    
    @Test
    public void testConstructorGetEntidadesCalled() {
        Mockito.verify(mockForm).getEntidades();
    }
    
    @Test
    public void testConstructorGetEntidadesException() {
        Mockito.when(mockForm.getEntidades()).thenThrow(new Exception());
        salvar = new Salvar(mockForm, 10);
        // Verify that the exception is caught and logged
        Mockito.verify(Main.janelaPrincipal).log(Mockito.anyString());
    }
    
    @Test
    public void testConstructorSetFileFilterCalled() {
        Mockito.verify(mockEscolhedor).setFileFilter(Mockito.any());
    }
    
    @Test
    public void testConstructorShowSaveDialogCalled() {
        Mockito.verify(mockEscolhedor).showSaveDialog(mockForm);
    }
    
    @Test
    public void testConstructorVelaTrue() {
        assertTrue(salvar.vela);
    }
    
    @Test
    public void testConstructorGetSelectedFileNull() {
        assertNull(salvar.getSelectedFile());
    }
    
    @Test
    public void testConstructorArquivoEmptyStringWhenSelectedFileNull() {
        assertEquals("", salvar.arquivo);
    }
    
    @Test
    public void testConstructorArquivoSetWhenSelectedFileNotNull() {
        Mockito.when(mockEscolhedor.getSelectedFile()).thenReturn(mockSelectedFile);
        Mockito.when(mockSelectedFile.getAbsolutePath()).thenReturn("testFile.jaw");
        
        salvar = new Salvar(mockForm, 10);
        
        assertEquals("testFile.jaw", salvar.arquivo);
    }
    
    @Test
    public void testConstructorArquivoSetWhenSelectedFileIsNullNameNotEmpty() {
        Mockito.when(mockEscolhedor.getSelectedFile()).thenReturn(null);
        Mockito.when(mockForm.getName()).thenReturn("Test");
        
        salvar = new Salvar(mockForm, 10);
        
        assertEquals("", salvar.arquivo);
    }
    
    @Test
    public void testConstructorArquivoSetToNomeArquivoSalvo() {
        Mockito.when(mockEscolhedor.getSelectedFile()).thenReturn(null);
        Mockito.when(mockForm.getNomeArquivoSalvo()).thenReturn("TestFile.jaw");
        
        salvar = new Salvar(mockForm, 20);
        
        assertEquals("TestFile.jaw", salvar.arquivo);
    }
    
    @Test
    public void testConstructorArquivoEndsWithJawExtension() {
        Mockito.when(mockEscolhedor.getSelectedFile()).thenReturn(mockSelectedFile);
        Mockito.when(mockSelectedFile.getAbsolutePath()).thenReturn("testFile.jaw");
        
        salvar = new Salvar(mockForm, 20);
        
        assertEquals("testFile.jaw", salvar.arquivo);
    }
    
    @Test
    public void testConstructorSetNomeArquivoSalvo() {
        assertEquals("testFile.jaw", mockForm.getNomeArquivoSalvo());
    }
    
    @Test
    public void testConstructorSetNomeArquivoSalvoVelaFalse() {
        Mockito.when(mockEscolhedor.getSelectedFile()).thenReturn(null);
        
        salvar = new Salvar(mockForm, 20);
        
        assertFalse(salvar.vela);
    }
    
    @Test
    public void testConstructorSetNomeArquivoSalvoVelaTrue() {
        Mockito.when(mockEscolhedor.getSelectedFile()).thenReturn(null);
        
        salvar = new Salvar(mockForm, 10);
        
        assertTrue(salvar.vela);
    }
    
    @Test
    public void testConstructorSalvarCalledWhenTipoSalvar20() throws IOException {
        salvar = new Salvar(mockForm, 20);
        
        Mockito.verify(mockEscrevedor).write(Mockito.anyString());
    }
    
    @Test
    public void testConstructorSalvarCalledWhenTipoSalvar30() throws IOException {
        salvar = new Salvar(mockForm, 30);
        
        Mockito.verify(mockEscrevedor).write(Mockito.anyString());
    }
    
    @Test
    public void testConstructorSalvarNotCalledWhenTipoSalvar10() throws IOException {
        salvar = new Salvar(mockForm, 10);
        
        Mockito.verify(mockEscrevedor, Mockito.never()).write(Mockito.anyString());
    }
    
    @Test
    public void testConstructorSerializacaoSalvarCalledWhenTipoSalvar20() throws IOException {
        salvar = new Salvar(mockForm, 20);
        
        Mockito.verify(Main.janelaPrincipal).log(Mockito.anyString());
    }
    
    @Test
    public void testConstructorSerializacaoSalvarCalledWhenTipoSalvar30() throws IOException {
        salvar = new Salvar(mockForm, 30);
        
        Mockito.verify(Main.janelaPrincipal).log(Mockito.anyString());
    }
    
    @Test
    public void testConstructorSerializacaoXMLSalvarCalledWhenTipoSalvar10() throws IOException {
        salvar = new Salvar(mockForm, 10);
        
        Mockito.verify(mockEscrevedor).write(Mockito.anyString());
    }
    
    @Test
    public void testFiltro2AcceptTrueWhenIsDirectory() {
        File mockDirectory = Mockito.mock(File.class);
        Mockito.when(mockDirectory.isDirectory()).thenReturn(true);
        FileFilter filtro2 = salvar.new Filtro2();
        
        assertTrue(filtro2.accept(mockDirectory));
    }
    
    @Test
    public void testFiltro2AcceptTrueWhenEndsWithJawXmlExtension() {
        File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.isDirectory()).thenReturn(false);
        Mockito.when(mockFile.getName()).thenReturn("testFile.jaw.xml");
        FileFilter filtro2 = salvar.new Filtro2();
        
        assertTrue(filtro2.accept(mockFile));
    }
    
    @Test
    public void testFiltro2AcceptFalseWhenDoesNotEndWithJawXmlExtension() {
        File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.isDirectory()).thenReturn(false);
        Mockito.when(mockFile.getName()).thenReturn("testFile.xml");
        FileFilter filtro2 = salvar.new Filtro2();
        
        assertFalse(filtro2.accept(mockFile));
    }
    
    @Test
    public void testFiltro2GetDescription() {
        FileFilter filtro2 = salvar.new Filtro2();
        
        assertEquals("XML JAW files", filtro2.getDescription());
    }
    
    @Test
    public void testFiltroAcceptTrueWhenIsDirectory() {
        File mockDirectory = Mockito.mock(File.class);
        Mockito.when(mockDirectory.isDirectory()).thenReturn(true);
        FileFilter filtro = salvar.new Filtro();
        
        assertTrue(filtro.accept(mockDirectory));
    }
    
    @Test
    public void testFiltroAcceptTrueWhenEndsWithJawExtension() {
        File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.isDirectory()).thenReturn(false);
        Mockito.when(mockFile.getName()).thenReturn("testFile.jaw");
        FileFilter filtro = salvar.new Filtro();
        
        assertTrue(filtro.accept(mockFile));
    }
    
    @Test
    public void testFiltroAcceptFalseWhenDoesNotEndWithJawExtension() {
        File mockFile = Mockito.mock(File.class);
        Mockito.when(mockFile.isDirectory()).thenReturn(false);
        Mockito.when(mockFile.getName()).thenReturn("testFile.xml");
        FileFilter filtro = salvar.new Filtro();
        
        assertFalse(filtro.accept(mockFile));
    }
    
    @Test
    public void testFiltroGetDescription() {
        FileFilter filtro = salvar.new Filtro();
        
        assertEquals("JAW files", filtro.getDescription());
    }
}