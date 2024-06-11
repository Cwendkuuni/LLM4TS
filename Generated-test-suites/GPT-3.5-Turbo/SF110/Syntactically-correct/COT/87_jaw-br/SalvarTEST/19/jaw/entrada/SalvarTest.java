
import jaw.entrada.*;
import jaw.metamodelo.*;
import jaw.gui.*;
import java.util.*;
import jaw.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

import org.junit.Test;
import static org.junit.Assert.*;

public class SalvarTest {
  
  @Test
  public void testSalvar() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = Salvar.SALVAR;
    Salvar salvar = new Salvar(form, tipoSalvar);
    // Verify that the 'vela' field is set to false
    assertFalse(salvar.vela);
    // Verify that the 'arquivo' field is empty
    assertEquals("", salvar.arquivo);
    // Verify that the 'xml' field is empty
    assertEquals("", salvar.xml);
    // Verify that the 'entidade' list is empty
    assertTrue(salvar.entidade.isEmpty());
    // Verify that the 'entidade' list is populated after calling 'form.getEntidades()'
    // This can be done by checking if the size of the 'entidade' list is greater than 0
    assertTrue(salvar.entidade.size() > 0);
  }
  
  @Test
  public void testSalvarComTudo() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = Salvar.SALVAR_COMO;
    Salvar salvar = new Salvar(form, tipoSalvar);
    // Verify that the 'vela' field is set to true
    assertTrue(salvar.vela);
    // Verify that the 'arquivo' field is empty
    assertEquals("", salvar.arquivo);
    // Verify that the 'xml' field is empty
    assertEquals("", salvar.xml);
    // Verify that the 'entidade' list is empty
    assertTrue(salvar.entidade.isEmpty());
    // Verify that the 'entidade' list is populated after calling 'form.getEntidades()'
    // This can be done by checking if the size of the 'entidade' list is greater than 0
    assertTrue(salvar.entidade.size() > 0);
    // Verify that the 'escolhedor' file chooser is initialized with the current directory set to 'Main.configuracao.getCaminhoDosProjetos()'
    JFileChooser escolhedor = salvar.new Filtro().escolhedor;
    assertEquals(Main.configuracao.getCaminhoDosProjetos(), escolhedor.getCurrentDirectory().getAbsolutePath());
    // Verify that the 'escolhedor' file chooser is set with the 'Filtro' file filter when the 'tipoSalvar' is 20 or 30
    if (tipoSalvar == Salvar.SALVAR || tipoSalvar == Salvar.SALVAR_COMO) {
      assertTrue(escolhedor.getFileFilter() instanceof Salvar.Filtro);
    }
    // Verify that the 'escolhedor' file chooser is set with the 'Filtro2' file filter when the 'tipoSalvar' is 10
    if (tipoSalvar == Salvar.SALVAR_XML) {
      assertTrue(escolhedor.getFileFilter() instanceof Salvar.Filtro2);
    }
    // Verify that the 'escolhedor' file chooser is shown to the user
    // This can be done by checking if the 'showSaveDialog' method is called on 'escolhedor' object
    // Also, 'showSaveDialog' returns 'JFileChooser.APPROVE_OPTION' after the dialog is successfully shown
    assertEquals(JFileChooser.APPROVE_OPTION, escolhedor.showSaveDialog(form));
    // Verify that the 'vela' field is set to true if 'tipoSalvar' is 10, 30 or (20 and 'form.getNomeArquivoSalvo()' is "Novo Projeto")
    assertTrue(salvar.vela);
    // Verify that the 'arquivo' field is not empty after selecting a file in the 'escolhedor' file chooser
    assertNotNull(salvar.arquivo);
    // Verify that the '.jaw' extension is added to the 'arquivo' field if 'tipoSalvar' is 20 or 30 and 'arquivo' doesn't end with '.jaw'
    if ((tipoSalvar == Salvar.SALVAR || tipoSalvar == Salvar.SALVAR_COMO) && !salvar.arquivo.endsWith(".jaw")) {
      assertTrue(salvar.arquivo.endsWith(".jaw"));
    }
    // Verify that the '.jaw.xml' extension is added to the 'arquivo' field if 'tipoSalvar' is 10 and 'arquivo' doesn't end with '.jaw.xml'
    if (tipoSalvar == Salvar.SALVAR_XML && !salvar.arquivo.endsWith(".jaw.xml")) {
      assertTrue(salvar.arquivo.endsWith(".jaw.xml"));
    }
    // Verify that the 'Serializacao' object is created and the 'salvar' method is called on it when 'tipoSalvar' is 20 or 30
    if (tipoSalvar == Salvar.SALVAR || tipoSalvar == Salvar.SALVAR_COMO) {
      Serializacao serializacao = salvar.new Serializacao();
      try {
        assertTrue(serializacao.salvar(salvar.entidade, salvar.arquivo));
      } catch (Exception ex) {
        fail();
      }
    }
    // Verify that the 'SerializacaoXML' object is created and the 'entidades2XML' method is called on it when 'tipoSalvar' is 10
    if (tipoSalvar == Salvar.SALVAR_XML) {
      SerializacaoXML serializacaoXML = salvar.new SerializacaoXML();
      try {
        assertNotNull(serializacaoXML.entidades2XML(salvar.entidade));
      } catch (Exception ex) {
        fail();
      }
    }
  }
  
  @Test
  public void testSalvarWithException() {
    ProcessarEntidades form = new ProcessarEntidades();
    int tipoSalvar = Salvar.SALVAR_COMO;
    Salvar salvar = new Salvar(form, tipoSalvar);
    // Verify that the 'vela' field is set to false
    assertFalse(salvar.vela);
    // Verify that the 'arquivo' field is empty
    assertEquals("", salvar.arquivo);
    // Verify that the 'xml' field is empty
    assertEquals("", salvar.xml);
    // Verify that the 'entidade' list is empty
    assertTrue(salvar.entidade.isEmpty());
    // Verify that the 'entidade' list is populated after calling 'form.getEntidades()'
    // This can be done by checking if the size of the 'entidade' list is greater than 0
    assertTrue(salvar.entidade.size() > 0);
    // Verify that the 'escolhedor' file chooser is initialized with the current directory set to 'Main.configuracao.getCaminhoDosProjetos()'
    JFileChooser escolhedor = salvar.new Filtro().escolhedor;
    assertEquals(Main.configuracao.getCaminhoDosProjetos(), escolhedor.getCurrentDirectory().getAbsolutePath());
    // Verify that the 'escolhedor' file chooser is set with the 'Filtro' file filter when the 'tipoSalvar' is 20 or 30
    if (tipoSalvar == Salvar.SALVAR || tipoSalvar == Salvar.SALVAR_COMO) {
      assertTrue(escolhedor.getFileFilter() instanceof Salvar.Filtro);
    }
    // Verify that the 'escolhedor' file chooser is set with the 'Filtro2' file filter when the 'tipoSalvar' is 10
    if (tipoSalvar == Salvar.SALVAR_XML) {
      assertTrue(escolhedor.getFileFilter() instanceof Salvar.Filtro2);
    }
    // Verify that the 'escolhedor' file chooser is shown to the user
    // This can be done by checking if the 'showSaveDialog' method is called on 'escolhedor' object
    // Also, 'showSaveDialog' returns 'JFileChooser.APPROVE_OPTION' after the dialog is successfully shown
    assertEquals(JFileChooser.APPROVE_OPTION, escolhedor.showSaveDialog(form));
    // Verify that the 'vela' field is set to true if 'tipoSalvar' is 10, 30 or (20 and 'form.getNomeArquivoSalvo()' is "Novo Projeto")
    assertTrue(salvar.vela);
    // Verify that the 'arquivo' field is not empty after selecting a file in the 'escolhedor' file chooser
    assertNotNull(salvar.arquivo);
    // Verify that the '.jaw' extension is added to the 'arquivo' field if 'tipoSalvar' is 20 or 30 and 'arquivo' doesn't end with '.jaw'
    if ((tipoSalvar == Salvar.SALVAR || tipoSalvar == Salvar.SALVAR_COMO) && !salvar.arquivo.endsWith(".jaw")) {
      assertTrue(salvar.arquivo.endsWith(".jaw"));
    }
    // Verify that the '.jaw.xml' extension is added to the 'arquivo' field if 'tipoSalvar' is 10 and 'arquivo' doesn't end with '.jaw.xml'
    if (tipoSalvar == Salvar.SALVAR_XML && !salvar.arquivo.endsWith(".jaw.xml")) {
      assertTrue(salvar.arquivo.endsWith(".jaw.xml"));
    }
    // Verify that the 'Serializacao' object is created and the 'salvar' method is called on it when 'tipoSalvar' is 20 or 30
    if (tipoSalvar == Salvar.SALVAR || tipoSalvar == Salvar.SALVAR_COMO) {
      Serializacao serializacao = salvar.new Serializacao();
      try {
        assertFalse(serializacao.salvar(null, salvar.arquivo));
      } catch (Exception ex) {
        assertNotNull(ex);
      }
    }
    // Verify that the 'SerializacaoXML' object is created and the 'entidades2XML' method is called on it when 'tipoSalvar' is 10
    if (tipoSalvar == Salvar.SALVAR_XML) {
      SerializacaoXML serializacaoXML = salvar.new SerializacaoXML();
      try {
        assertNotNull(serializacaoXML.entidades2XML(null));
      } catch (Exception ex) {
        assertNotNull(ex);
      }
    }
  }
  
  @Test
  public void testFiltro2() {
    Salvar.Filtro2 filtro2 = new Salvar().new Filtro2();
    // Verify that the 'accept' method returns true when the file is a directory or it ends with '.jaw.xml'
    assertTrue(filtro2.accept(new File("directory")));
    assertTrue(filtro2.accept(new File("file.jaw.xml")));
    assertFalse(filtro2.accept(new File("file.jaw")));
    assertFalse(filtro2.accept(new File("file.xml")));
    assertFalse(filtro2.accept(new File("file.txt")));
    assertFalse(filtro2.accept(new File("")));
    // Verify that the 'getDescription' method returns "XML JAW files"
    assertEquals("XML JAW files", filtro2.getDescription());
  }
  
  @Test
  public void testFiltro() {
    Salvar.Filtro filtro = new Salvar().new Filtro();
    // Verify that the 'accept' method returns true when the file is a directory or it ends with '.jaw'
    assertTrue(filtro.accept(new File("directory")));
    assertTrue(filtro.accept(new File("file.jaw")));
    assertFalse(filtro.accept(new File("file.jaw.xml")));
    assertFalse(filtro.accept(new File("file.xml")));
    assertFalse(filtro.accept(new File("file.txt")));
    assertFalse(filtro.accept(new File("")));
    // Verify that the 'getDescription' method returns "JAW files"
    assertEquals("JAW files", filtro.getDescription());
  }
}
