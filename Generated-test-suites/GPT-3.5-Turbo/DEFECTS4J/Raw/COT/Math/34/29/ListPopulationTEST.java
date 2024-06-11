import org.junit.*;

public class ListPopulationTest {

    @Test
    public void testListPopulationConstructor() {
        ListPopulation population = new ListPopulation(10);
        Assert.assertEquals(0, population.getPopulationSize());
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    @Test
    public void testListPopulationConstructorWithChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        ListPopulation population = new ListPopulation(chromosomes, 10);
        Assert.assertEquals(2, population.getPopulationSize());
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testListPopulationConstructorWithInvalidPopulationLimit() {
        new ListPopulation(-10);
    }

    @Test(expected = NullArgumentException.class)
    public void testListPopulationConstructorWithNullChromosomes() {
        new ListPopulation(null, 10);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testListPopulationConstructorWithTooManyChromosomes() {
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        new ListPopulation(chromosomes, 2);
    }

    @Test
    public void testSetChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        Assert.assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NullArgumentException.class)
    public void testSetChromosomesWithNullChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.setChromosomes(null);
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testSetChromosomesWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
    }

    @Test
    public void testAddChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosomes(Arrays.asList(new Chromosome(), new Chromosome()));
        Assert.assertEquals(2, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomesWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation(2);
        population.addChromosomes(Arrays.asList(new Chromosome(), new Chromosome(), new Chromosome()));
    }

    @Test
    public void testGetChromosomes() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        Assert.assertEquals(chromosomes, population.getChromosomes());
    }

    @Test
    public void testGetChromosomeList() {
        ListPopulation population = new ListPopulation(10);
        List<Chromosome> chromosomes = new ArrayList<>();
        chromosomes.add(new Chromosome());
        chromosomes.add(new Chromosome());
        population.setChromosomes(chromosomes);
        Assert.assertEquals(chromosomes, population.getChromosomeList());
    }

    @Test
    public void testAddChromosome() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new Chromosome());
        Assert.assertEquals(1, population.getPopulationSize());
    }

    @Test(expected = NumberIsTooLargeException.class)
    public void testAddChromosomeWithTooManyChromosomes() {
        ListPopulation population = new ListPopulation(1);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
    }

    @Test
    public void testGetFittestChromosome() {
        Chromosome chromosome1 = new Chromosome();
        chromosome1.setFitness(10);
        Chromosome chromosome2 = new Chromosome();
        chromosome2.setFitness(20);

        ListPopulation population = new ListPopulation(10);
        population.addChromosome(chromosome1);
        population.addChromosome(chromosome2);

        Assert.assertEquals(chromosome2, population.getFittestChromosome());
    }

    @Test
    public void testGetPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        Assert.assertEquals(10, population.getPopulationLimit());
    }

    @Test(expected = NotPositiveException.class)
    public void testSetPopulationLimitWithInvalidPopulationLimit() {
        ListPopulation population = new ListPopulation(10);
        population.setPopulationLimit(-10);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testSetPopulationLimitWithLowerLimitThanChromosomes() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new Chromosome());
        population.setPopulationLimit(8);
    }

    @Test
    public void testGetPopulationSize() {
        ListPopulation population = new ListPopulation(10);
        population.addChromosome(new Chromosome());
        population.addChromosome(new Chromosome());
        Assert.assertEquals(2, population.getPopulationSize());
    }

    @Test
    public void testIterator() {
        ListPopulation population = new ListPopulation(10);
        Chromosome chromosome = new Chromosome();
        population.addChromosome(chromosome);
        Iterator<Chromosome> iterator = population.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(chromosome, iterator.next());
        Assert.assertFalse(iterator.hasNext());
    }
}