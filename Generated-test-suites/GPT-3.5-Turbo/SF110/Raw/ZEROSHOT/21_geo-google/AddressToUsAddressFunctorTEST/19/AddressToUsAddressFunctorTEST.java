package geo.google.mapping;

import static org.junit.Assert.*;
import org.junit.Test;
import geo.google.datamodel.*;
import geo.google.*;
import org.apache.commons.collections.*;
import oasis.names.tc.ciq.xsdschema.xal._2.*;
import java.util.*;

public class AddressToUsAddressFunctorTest {

  @Test
  public void testExecute() {

    AddressToUsAddressFunctor functor = AddressToUsAddressFunctor.getInstance();

    // Test case 1: addr with all attributes
    GeoAddress addr1 = new GeoAddress();
    AddressDetails addressDetails = new AddressDetails();
    AddressDetails.Country country1 = new AddressDetails.Country();
    List<AddressDetails.Country.CountryNameCode> countryNames = new ArrayList<>();
    AddressDetails.Country.CountryNameCode countryName = new AddressDetails.Country.CountryNameCode();
    countryName.setContent("USA");
    countryNames.add(countryName);

    AdministrativeArea adminArea = new AdministrativeArea();
    List<AdministrativeArea.AdministrativeAreaName> adminAreaNames = new ArrayList<>();
    AdministrativeArea.AdministrativeAreaName adminAreaName = new AdministrativeArea.AdministrativeAreaName();
    adminAreaName.setContent("California");
    adminAreaNames.add(adminAreaName);
    adminArea.setAdministrativeAreaName(adminAreaNames);

    AdministrativeArea.SubAdministrativeArea subAdminArea = new AdministrativeArea.SubAdministrativeArea();
    List<AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName> subAreaNames = new ArrayList<>();
    AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName subAreaName = new AdministrativeArea.SubAdministrativeArea.SubAdministrativeAreaName();
    subAreaName.setContent("Santa Clara");
    subAreaNames.add(subAreaName);
    subAdminArea.setSubAdministrativeAreaName(subAreaNames);
    adminArea.setSubAdministrativeArea(subAdminArea);

    Locality locality = new Locality();
    List<Locality.LocalityName> localNames = new ArrayList<>();
    Locality.LocalityName localName = new Locality.LocalityName();
    localName.setContent("San Jose");
    localNames.add(localName);
    locality.setLocalityName(localNames);

    PostalCode postalCode = new PostalCode();
    List<PostalCode.PostalCodeNumber> numbers = new ArrayList<>();
    PostalCode.PostalCodeNumber number = new PostalCode.PostalCodeNumber();
    number.setContent("95134");
    numbers.add(number);
    postalCode.setPostalCodeNumber(numbers);
    locality.setPostalCode(postalCode);

    Thoroughfare thoroughfare = new Thoroughfare();
    List<ThoroughfareNameType> thoroughfares = new ArrayList<>();
    ThoroughfareNameType thoroughfareName = new ThoroughfareNameType();
    thoroughfareName.setContent("1st Street");
    thoroughfares.add(thoroughfareName);
    thoroughfare.setThoroughfareName(thoroughfares);
    locality.setThoroughfare(thoroughfare);
    
    addressDetails.setCountry(country1);
    country1.setCountryNameCode(countryNames);
    country1.setAdministrativeArea(adminArea);
    adminArea.setLocality(locality);
    addr1.setAddressDetails(addressDetails);

    GeoUsAddress expected1 = new GeoUsAddress();
    expected1.setCountry("USA");
    expected1.setState("California");
    expected1.setCounty("Santa Clara");
    expected1.setCity("San Jose");
    expected1.setPostalCode("95134");
    expected1.setAddressLine1("1st Street");
    expected1.setAccuracy(1);

    GeoUsAddress result1 = functor.execute(addr1);
    assertEquals(expected1.getCountry(), result1.getCountry());
    assertEquals(expected1.getState(), result1.getState());
    assertEquals(expected1.getCounty(), result1.getCounty());
    assertEquals(expected1.getCity(), result1.getCity());
    assertEquals(expected1.getPostalCode(), result1.getPostalCode());
    assertEquals(expected1.getAddressLine1(), result1.getAddressLine1());
    assertEquals(expected1.getAccuracy(), result1.getAccuracy());

    // Test case 2: addr with null values
    GeoAddress addr2 = new GeoAddress();
    GeoUsAddress expected2 = new GeoUsAddress();

    GeoUsAddress result2 = functor.execute(addr2);
    assertEquals(expected2, result2);

    // Test case 3: addr with only coordinate and accuracy
    GeoAddress addr3 = new GeoAddress();
    Coordinate coordinate = new Coordinate();
    addr3.setCoordinate(coordinate);
    addr3.setAccuracy(0.9);

    GeoUsAddress expected3 = new GeoUsAddress();
    expected3.setCoordinate(coordinate.clone());
    expected3.setAccuracy(0.9);

    GeoUsAddress result3 = functor.execute(addr3);
    assertEquals(expected3.getCoordinate(), result3.getCoordinate());
    assertEquals(expected3.getAccuracy(), result3.getAccuracy());

  }
}