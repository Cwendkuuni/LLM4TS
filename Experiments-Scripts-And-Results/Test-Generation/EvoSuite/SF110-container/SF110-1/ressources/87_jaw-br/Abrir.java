package jaw.entrada;

import jaw.gui.*;
import jaw.*;
import javax.swing.filechooser.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

public class Abrir
{
    public String xml;
    public ProcessarEntidades form;
    public static final int ABRIR_XML = 10;
    public static final int ABRIR = 20;
    
    public Abrir(final ProcessarEntidades form, final String arquivo) {
        this.xml = "";
        this.form = form;
        if (arquivo.endsWith(".jaw.xml")) {
            this.abrirXML(arquivo);
        }
        else {
            this.abrirSerializado(arquivo);
        }
    }
    
    public Abrir(final ProcessarEntidades form, final int tipoAbrir) {
        this.xml = "";
        this.form = form;
        final JFileChooser escolhedor = new JFileChooser();
        escolhedor.setCurrentDirectory(new File(Main.configuracao.getCaminhoDosProjetos()));
        if (tipoAbrir == 10) {
            escolhedor.setFileFilter(new Filtro2());
        }
        if (tipoAbrir == 20) {
            escolhedor.setFileFilter(new Filtro());
        }
        escolhedor.showOpenDialog(null);
        if (escolhedor.getSelectedFile() == null) {
            return;
        }
        final String arquivo = escolhedor.getSelectedFile().getAbsolutePath();
        if (tipoAbrir == 20) {
            this.abrirSerializado(arquivo);
        }
        if (tipoAbrir == 10) {
            this.abrirXML(arquivo);
        }
    }
    
    private void abrirSerializado(final String arquivo) {
        if (arquivo.endsWith(".jaw")) {
            try {
                this.form.setNomeArquivoSalvo(arquivo);
                this.form.setEntidades(new Serializacao().abrir(arquivo));
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Arquivo jaw corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!", "Abrir Arquivo jaw", 0);
                Main.janelaPrincipal.log(e.toString());
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Arquivo jaw corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!", "Abrir Arquivo jaw", 0);
        }
    }
    
    private void abrirXML(final String arquivo) {
        if (arquivo.endsWith(".jaw.xml")) {
            try {
                this.form.setNomeArquivoSalvo(arquivo);
                final Reader leitor = new FileReader(arquivo);
                this.form.setEntidades(new SerializacaoXML().xml2Entidades(leitor));
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Arquivo xml corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!", "Importa\u00c3§\u00c3£o XML", 0);
                Main.janelaPrincipal.log(e.toString());
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Arquivo xml corrompido ou incompat\u00c3\u00advel com aplica\u00c3§\u00c3£o!", "Importa\u00c3§\u00c3£o XML", 0);
        }
    }
    
    private class Filtro2 extends FileFilter
    {
        @Override
        public boolean accept(final File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
        }
        
        @Override
        public String getDescription() {
            return "XML JAW files";
        }
    }
    
    private class Filtro extends FileFilter
    {
        @Override
        public boolean accept(final File f) {
            return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
        }
        
        @Override
        public String getDescription() {
            return "JAW files";
        }
    }
}
